import { useEffect, useState } from "react";
import { apiRequest, getErrorMessage, getList } from "../api/api.js";

const emptyBook = { title: "", author: "", isbn: "", quantity: "1" };

// Display the books list and handle adding, editing, and deleting books.
export default function Books() {
  const [books, setBooks] = useState([]);
  const [form, setForm] = useState(emptyBook);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  // Load all books from the backend when this page is first shown.
  async function loadBooks() {
    setLoading(true);
    setError("");
    try {
      setBooks(getList(await apiRequest("/books")));
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadBooks();
  }, []);

  // Update the input that the user changed.
  function handleChange(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  // Validate the form, then create a book or save its edits.
  async function handleSubmit(event) {
    event.preventDefault();
    setMessage("");
    setError("");
    const quantity = Number(form.quantity);

    if (!form.title.trim() || !form.author.trim() || !form.isbn.trim()) {
      setError("Enter a title, author, and ISBN.");
      return;
    }
    if (!Number.isInteger(quantity) || quantity < 0) {
      setError("Quantity must be a whole number that is zero or greater.");
      return;
    }

    const payload = {
      title: form.title.trim(),
      author: form.author.trim(),
      isbn: form.isbn.trim(),
      quantity,
    };

    try {
      if (editingId !== null) {
        await apiRequest(`/books/${encodeURIComponent(editingId)}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        setMessage("Book updated successfully.");
      } else {
        await apiRequest("/books", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        setMessage("Book added successfully.");
      }
      setForm(emptyBook);
      setEditingId(null);
      await loadBooks();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Put the selected book's details into the form for editing.
  function startEditing(book) {
    setEditingId(book.id);
    setForm({
      title: book.title || "",
      author: book.author || "",
      isbn: book.isbn || "",
      quantity: String(book.quantity ?? book.totalCopies ?? 1),
    });
    setMessage("");
    setError("");
  }

  // Ask for confirmation before permanently deleting a book.
  async function deleteBook(book) {
    if (!window.confirm(`Delete "${book.title || "this book"}"?`)) return;
    setMessage("");
    setError("");
    try {
      await apiRequest(`/books/${encodeURIComponent(book.id)}`, { method: "DELETE" });
      setMessage("Book deleted successfully.");
      if (editingId === book.id) {
        setEditingId(null);
        setForm(emptyBook);
      }
      await loadBooks();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    }
  }

  // Clear the form and leave edit mode.
  function cancelEditing() {
    setEditingId(null);
    setForm(emptyBook);
    setMessage("");
    setError("");
  }

  return (
    <section>
      <div className="page-heading">
        <div>
          <p className="eyebrow">CATALOG</p>
          <h1>Books</h1>
          <p className="subheading">Keep your collection organized and up to date.</p>
        </div>
        <div className="count-chip"><strong>{books.length}</strong> titles</div>
      </div>

      <div className="content-grid">
        <section className="card form-card">
          <div className="card-heading">
            <div>
              <h2>{editingId !== null ? "Edit book" : "Add a book"}</h2>
              <p>Enter the book details below.</p>
            </div>
            <span className="heading-icon" aria-hidden="true">＋</span>
          </div>
          <form onSubmit={handleSubmit}>
            <label>
              Title
              <input name="title" value={form.title} onChange={handleChange} placeholder="e.g. The Great Gatsby" required />
            </label>
            <label>
              Author
              <input name="author" value={form.author} onChange={handleChange} placeholder="e.g. F. Scott Fitzgerald" required />
            </label>
            <label>
              ISBN
              <input name="isbn" value={form.isbn} onChange={handleChange} placeholder="e.g. 9780743273565" required />
            </label>
            <label>
              Quantity
              <input name="quantity" type="number" min="0" step="1" value={form.quantity} onChange={handleChange} required />
            </label>
            {error && <p className="notice error-notice" role="alert">{error}</p>}
            {message && <p className="notice success-notice" role="status">{message}</p>}
            <div className="form-actions">
              <button className="button primary-button" type="submit">{editingId !== null ? "Save changes" : "Add book"}</button>
              {editingId !== null && <button className="button secondary-button" onClick={cancelEditing} type="button">Cancel</button>}
            </div>
          </form>
        </section>

        <section className="card table-card">
          <div className="card-heading table-heading">
            <div>
              <h2>Book collection</h2>
              <p>Books currently registered in your library.</p>
            </div>
            <button className="button secondary-button refresh-button" onClick={loadBooks} type="button">Refresh</button>
          </div>
          {loading ? (
            <p className="empty-state">Loading books…</p>
          ) : books.length === 0 ? (
            <p className="empty-state">No books yet. Add your first book using the form.</p>
          ) : (
            <div className="table-scroll">
              <table>
                <thead><tr><th>Title</th><th>Author</th><th>ISBN</th><th>Qty.</th><th>Actions</th></tr></thead>
                <tbody>
                  {books.map((book) => (
                    <tr key={book.id}>
                      <td className="primary-cell">{book.title || "—"}</td>
                      <td>{book.author || "—"}</td>
                      <td>{book.isbn || "—"}</td>
                      <td>{book.quantity ?? book.totalCopies ?? "—"}</td>
                      <td className="action-cell">
                        <button className="text-button" onClick={() => startEditing(book)} type="button">Edit</button>
                        <button className="text-button delete-button" onClick={() => deleteBook(book)} type="button">Delete</button>
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
