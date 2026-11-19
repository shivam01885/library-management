import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// Vite config: enables React and JSX support.
export default defineConfig({
  plugins: [react()],
});