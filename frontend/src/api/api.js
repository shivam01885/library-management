// Keep the backend address in one place so it is easy to change.
export const API_URL = "http://localhost:8080/api";

// Send a request and turn backend responses into useful JavaScript values.
export async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { "Content-Type": "application/json" } : {}),
      ...options.headers,
    },
  });
  const responseText = await response.text();
  let result = null;

  if (responseText) {
    try {
      result = JSON.parse(responseText);
    } catch {
      result = responseText;
    }
  }

  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;
    if (result && typeof result === "object") {
      message = result.message || result.error || message;
      if (Array.isArray(result.errors) && result.errors.length > 0) {
        message += `: ${result.errors.join("; ")}`;
      }
    } else if (typeof result === "string" && result) {
      message = result;
    }
    throw new Error(message);
  }

  return result;
}

// Read a list from either a plain array or a common wrapped API response.
export function getList(result) {
  if (Array.isArray(result)) return result;
  if (result && Array.isArray(result.data)) return result.data;
  if (result && Array.isArray(result.content)) return result.content;
  return [];
}

// Convert API errors into a message suitable for an alert near the form.
export function getErrorMessage(error) {
  return error instanceof Error ? error.message : "Something went wrong.";
}