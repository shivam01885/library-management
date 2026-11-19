const tabs = ["Books", "Members", "Issue-Return"];

// Show the application title and the currently selected section.
export default function Navbar({ activeTab, onChangeTab }) {
  return (
    <header className="topbar">
      <a className="brand" href="#" onClick={(event) => event.preventDefault()}>
        <span className="brand-mark" aria-hidden="true">L</span>
        <span>Library<span className="brand-light">Desk</span></span>
      </a>
      <nav className="tabs" aria-label="Main navigation">
        {tabs.map((tab) => (
          <button
            className={`tab-button ${activeTab === tab ? "active" : ""}`}
            key={tab}
            onClick={() => onChangeTab(tab)}
            type="button"
          >
            {tab}
          </button>
        ))}
      </nav>
      <span className="topbar-note">Management portal</span>
    </header>
  );
}
