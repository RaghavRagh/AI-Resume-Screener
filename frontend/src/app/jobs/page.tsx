"use client";

import axios from "axios";
import { useState } from "react";

const JobPage = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async () => {
    if (!title || !description) {
      setError("Please fill in all fields.");
      return;
    }

    setError("");
    setLoading(true);
    setMessage("");

    try {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/jobs`,
        { title, description }
      );

      setMessage(res.data.message);
      setTitle("");
      setDescription("");
    } catch (err) {
      console.error(err);
      setError("Failed to create job.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen flex flex-col items-center justify-center gap-4 px-4">
      <h1 className="text-3xl font-bold">Create Job Description 🧾</h1>

      <input
        type="text"
        placeholder="Job Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full max-w-xl border px-4 py-2 rounded"
      />

      <textarea
        placeholder="Job Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        rows={6}
        className="w-full max-w-xl border px-4 py-2 rounded"
      />

      <button
        onClick={handleSubmit}
        disabled={loading}
        className={`px-6 py-2 rounded text-black ${
          loading ? "bg-gray-400" : "bg-white"
        }`}
      >
        {loading ? "Saving..." : "Create Job"}
      </button>

      {error && <p className="text-red-500">{error}</p>}
      {message && <p className="text-green-600">{message}</p>}
    </main>
  );
};

export default JobPage;
