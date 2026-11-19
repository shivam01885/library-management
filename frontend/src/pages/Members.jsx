import { useEffect, useState } from "react";
import { apiRequest, getErrorMessage, getList } from "../api/api.js";

const emptyMember = { name: "", email: "", phone: "" };

// Display members and handle creating, editing, and deleting member records.
export default function Members() {
  const [members, setMembers] = useState([]);
  const [form, setForm] = useState(emptyMember);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  // Load the latest member list from the backend.
  async function loadMembers() {
    setLoading(true);
    setError("");
    try {
      setMembers(getList(await apiRequest("/members")));
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadMembers();
  }, []);

  // Update the input that the user changed.
  function handleChange(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  // Validate member details, then create a member or save edits.
  async function handleSubmit(event) {
    event.preventDefault();
    setMessage("");
    setError("");
    const name = form.name.trim();
    const email = form.email.trim();
    const phone = form.phone.trim();

    if (!name || !email || !phone) {
      setError("Enter a name, email address, and phone number.");
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError("Enter a valid email address.");
      return;
    }
    if (!/^[0-9]{10}$/.test(phone)) {
      setError("Phone number must contain exactly 10 digits.");
      return;
    }

    const payload = { name, email, phone };
    try {
      if (editingId !== null) {
        await apiRequest(`/members/${encodeURIComponent(editingId)}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        setMessage("Member updated successfully.");
      } else {
        await apiRequest("/members", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        setMessage("Member added successfully.");
      }
      setForm(emptyMember);
      setEditingId(null);
      await loadMembers();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Copy the chosen member details into the form.
  function startEditing(member) {
    setEditingId(member.id);
    setForm({
      name: member.name || "",
      email: member.email || "",
      phone: member.phone || "",
    });
    setMessage("");
    setError("");
  }

  // Delete a member after the user confirms the action.
  async function deleteMember(member) {
    if (!window.confirm(`Delete "${member.name || "this member"}"?`)) return;
    setMessage("");
    setError("");
    try {
      await apiRequest(`/members/${encodeURIComponent(member.id)}`, { method: "DELETE" });
      setMessage("Member deleted successfully.");
      if (editingId === member.id) {
        setEditingId(null);
        setForm(emptyMember);
      }
      await loadMembers();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Clear the form and exit edit mode.
  function cancelEditing() {
    setEditingId(null);
    setForm(emptyMember);
    setMessage("");
    setError("");
  }

  return (
    <section>
      <div className="page-heading">
        <div>
          <p className="eyebrow">PEOPLE</p>
          <h1>Members</h1>
          <p className="subheading">Manage the people who borrow from your library.</p>
        </div>
        <div className="count-chip"><strong>{members.length}</strong> members</div>
      </div>

      <div className="content-grid">
        <section className="card form-card">
          <div className="card-heading">
            <div>
              <h2>{editingId !== null ? "Edit member" : "Add a member"}</h2>
              <p>Enter the member's contact details.</p>
            </div>
            <span className="heading-icon member-icon" aria-hidden="true">♙</span>
          </div>
          <form onSubmit={handleSubmit}>
            <label>
              Full name
              <input name="name" value={form.name} onChange={handleChange} placeholder="e.g. Jordan Lee" required />
            </label>
            <label>
              Email address
              <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="jordan@example.com" required />
            </label>
            <label>
              Phone number
              <input name="phone" type="tel" value={form.phone} onChange={handleChange} placeholder="e.g. 5550100100" pattern="[0-9]{10}" maxLength="10" required />
            </label>
            {error && <p className="notice error-notice" role="alert">{error}</p>}
            {message && <p className="notice success-notice" role="status">{message}</p>}
            <div className="form-actions">
              <button className="button primary-button" type="submit">{editingId !== null ? "Save changes" : "Add member"}</button>
              {editingId !== null && <button className="button secondary-button" onClick={cancelEditing} type="button">Cancel</button>}
            </div>
          </form>
        </section>

        <section className="card table-card">
          <div className="card-heading table-heading">
            <div>
              <h2>Member directory</h2>
              <p>Registered library members.</p>
            </div>
            <button className="button secondary-button refresh-button" onClick={loadMembers} type="button">Refresh</button>
          </div>
          {loading ? (
            <p className="empty-state">Loading members…</p>
          ) : members.length === 0 ? (
            <p className="empty-state">No members yet. Add your first member using the form.</p>
          ) : (
            <div className="table-scroll">
              <table>
                <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Actions</th></tr></thead>
                <tbody>
                  {members.map((member) => (
                    <tr key={member.id}>
                      <td className="primary-cell">{member.name || "—"}</td>
                      <td>{member.email || "—"}</td>
                      <td>{member.phone || "—"}</td>
                      <td className="action-cell">
                        <button className="text-button" onClick={() => startEditing(member)} type="button">Edit</button>
                        <button className="text-button delete-button" onClick={() => deleteMember(member)} type="button">Delete</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </section>
  );
}
