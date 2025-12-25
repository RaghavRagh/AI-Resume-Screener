"use client";

import axios from "axios";
import { useRouter } from "next/navigation";
import { useState } from "react";

const UploadPage = () => {
  const router = useRouter();

  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a file to upload!");
      return;
    }

    setError("");
    setLoading(true);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/resumes/upload`,
        formData
      );

      router.push("/match?resumeId=" + res.data.resumeId);
    } catch (err) {
      console.log(err);
      setError("Something went wrong. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="h-screen flex flex-col items-center justify-center gap-4">
      <h2 className="text-2xl font-semibold">Upload Resume 📄</h2>

      <input
        type="file"
        className="border border-white py-2 px-4 rounded-lg hover:cursor-pointer"
        accept="application/pdf"
        onChange={(e) => setFile(e.target.files?.[0] || null)}
      />

      <button
        onClick={handleUpload}
        disabled={loading}
        className={`px-4 py-2 bg-white ${
          loading && "bg-neutral-200"
        } text-black rounded hover:cursor-pointer hover:bg-neutral-200`}
      >
        {loading ? "Uploading..." : "Upload"}
      </button>

      {error && <p className="text-red-500">{error}</p>}
    </main>
  );
}

export default UploadPage;