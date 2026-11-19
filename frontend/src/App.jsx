import React, { useState } from "react";
// import { useState } from "react";
import Navbar from "./components/Navbar.jsx";
import Books from "./pages/Books.jsx";
import Members from "./pages/Members.jsx";
import IssueReturn from "./pages/IssueReturn.jsx";

// Switch between the three library workflows without a routing library.
export default function App() {
  const [activeTab, setActiveTab] = useState("Books");

  return (
    <div className="app-shell">
      <Navbar activeTab={activeTab} onChangeTab={setActiveTab} />
      <main className="main-content">
        {activeTab === "Books" && <Books />}
        {activeTab === "Members" && <Members />}
        {activeTab === "Issue-Return" && <IssueReturn />}
      </main>
      <footer className="app-footer">Library Management System</footer>
    </div>
  );
}
