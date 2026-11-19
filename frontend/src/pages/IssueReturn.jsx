import { useEffect, useState } from "react";
import { apiRequest, getErrorMessage, getList } from "../api/api.js";

// Find an associated record ID whether the API returns an object or an ID.
function relatedId(value) {
  return value && typeof value === "object" ? value.id : value;
}

// Delete a loan after asking the user to confirm the action.
async function handleDelete(issue) {
  if (!window.confirm("Delete this lending record?")) return;
  setMessage("");
  setError("");
  try {
    await apiRequest(`/issues/${encodeURIComponent(issue.id)}`, { method: "DELETE" });
    setMessage("Lending record deleted successfully.");
    await loadData();
  } catch (requestError) {
    setError(getErrorMessage(requestError));
  }
}

// Keep numeric identifiers numeric and preserve non-numeric identifiers.
function requestId(value) {
  const numericId = Number(value);
  return value !== "" && Number.isFinite(numericId) ? numericId : value;
}

// Display lending activity and let users issue or return a book.
export default function IssueReturn() {
  const [books, setBooks] = useState([]);
  const [members, setMembers] = useState([]);
  const [issues, setIssues] = useState([]);
  const [bookId, setBookId] = useState("");
  const [memberId, setMemberId] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  // Load books, members, and lending records for the dropdowns and table.
  async function loadData() {
    setLoading(true);
    setError("");
    try {
      const [bookResult, memberResult, issueResult] = await Promise.all([
        apiRequest("/books"),
        apiRequest("/members"),
        apiRequest("/issues"),
      ]);
      setBooks(getList(bookResult));
      setMembers(getList(memberResult));
      setIssues(getList(issueResult));
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  // Return true when a record represents an ongoing loan.
  function isActive(issue) {
    const status = String(issue.status || "").toLowerCase();
    return !issue.returnDate && status !== "returned" && status !== "return";
  }

  // Send a validated lending request to the backend.
  async function handleIssue(event) {
    event.preventDefault();
    setMessage("");
    setError("");
    if (!bookId || !memberId) {
      setError("Choose both a book and a member.");
      return;
    }

    try {
      await apiRequest("/issues", {
        method: "POST",
        body: JSON.stringify({ bookId: requestId(bookId), memberId: requestId(memberId) }),
      });
      setMessage("Book issued successfully.");
      setBookId("");
      setMemberId("");
      await loadData();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Mark the selected active loan as returned.
  async function handleReturn(issue) {
    setMessage("");
    setError("");
    try {
      await apiRequest(`/issues/${encodeURIComponent(issue.id)}/return`, { method: "PUT" });
      setMessage("Book returned successfully.");
      await loadData();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Get a readable book or member label for a lending record.
  function relatedName(issue, type) {
    const relation = type === "book" ? issue.book : issue.member;
    const fallbackId = type === "book" ? issue.bookId : issue.memberId;
    if (relation && typeof relation === "object") {
      return relation.title || relation.name || `ID ${relation.id ?? "—"}`;
    }
    const id = relatedId(relation) ?? fallbackId;
    const records = type === "book" ? books : members;
    const found = records.find((record) => String(record.id) === String(id));
    return found ? (type === "book" ? found.title : found.name) : (id ? `ID ${id}` : "—");
  }

  // Format an API date value in a simple human-readable way.
  function displayDate(value) {
    if (!value) return "—";
    return String(value);
  }

  const availableBooks = books.filter((book) => {
    const quantity = Number(book.availableCopies ?? book.quantity ?? book.totalCopies ?? 1);
    return quantity > 0;
  });

  return (
    <section>
      <div className="page-heading">
        <div>
          <p className="eyebrow">CIRCULATION</p>
          <h1>Issue &amp; return</h1>
          <p className="subheading">Track books going out and coming back to the library.</p>
        </div>
        <div className="count-chip"><strong>{issues.filter(isActive).length}</strong> active loans</div>
      </div>

      <div className="card issue-card">
        <div className="card-heading">
          <div>
            <h2>Issue a book</h2>
            <p>Select a member and an available book to record a loan.</p>
          </div>
          <span className="heading-icon issue-icon" aria-hidden="true">↗</span>
        </div>
        <form className="issue-form" onSubmit={handleIssue}>
          <label>
            Book
            <select value={bookId} onChange={(event) => setBookId(event.target.value)} required>
              <option value="">Choose a book</option>
              {availableBooks.map((book) => <option key={book.id} value={book.id}>{book.title} — {book.author}</option>)}
            </select>
          </label>
          <label>
            Member
            <select value={memberId} onChange={(event) => setMemberId(event.target.value)} required>
              <option value="">Choose a member</option>
              {members.map((member) => <option key={member.id} value={member.id}>{member.name}</option>)}
            </select>
          </label>
          <button className="button primary-button issue-submit" type="submit">Issue book</button>
        </form>
        {error && <p className="notice error-notice" role="alert">{error}</p>}
        {message && <p className="notice success-notice" role="status">{message}</p>}
        {(!availableBooks.length || !members.length) && (
          <p className="form-hint">Add at least one available book and one member before recording a loan.</p>
        )}
      </div>

      <section className="card table-card loans-card">
        <div className="card-heading table-heading">
          <div>
            <h2>Loan history</h2>
            <p>View all issues and mark active loans as returned.</p>
          </div>
          <button className="button secondary-button refresh-button" onClick={loadData} type="button">Refresh</button>
        </div>
        {loading ? (
          <p className="empty-state">Loading lending records…</p>
        ) : issues.length === 0 ? (
          <p className="empty-state">No lending records yet. Use the form above to issue a book.</p>
        ) : (
          <div className="table-scroll">
            <table>
              <thead><tr><th>Book</th><th>Member</th><th>Issued</th><th>Returned</th><th>Status</th><th>Actions</th></tr></thead>
              <tbody>
                {issues.map((issue) => (
                  <tr key={issue.id}>
                    <td className="primary-cell">{relatedName(issue, "book")}</td>
                    <td>{relatedName(issue, "member")}</td>
                    <td>{displayDate(issue.issueDate || issue.issuedAt || issue.createdAt)}</td>
                    <td>{displayDate(issue.returnDate || issue.returnedAt)}</td>
                    <td><span className={`status-pill ${isActive(issue) ? "status-active" : "status-returned"}`}>{isActive(issue) ? "Issued" : "Returned"}</span></td>
                    <td className="action-cell">
                      {isActive(issue) && (
                        <button className="text-button" onClick={() => handleReturn(issue)} type="button">Return book</button>
                      )}
                      <button className="text-button delete-button" onClick={() => handleDelete(issue)} type="button">Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </section>
  );
}
