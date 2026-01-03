"use client";

import axios from "axios";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Upload } from "lucide-react";

const UploadPage = () => {
  const router = useRouter();

  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [isUploaded, setIsUploaded] = useState(false);

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a file to upload!");
      return;
    }

    setError("");
    setLoading(true);
    setIsUploaded(false);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/resumes/upload`,
        formData
      );

      setIsUploaded(true);
      router.push("/match?resumeId=" + res.data.resumeId);
    } catch (err) {
      console.log(err);
      setError("Something went wrong. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="flex-1 overflow-auto bg-slate-100 h-screen">
      <div className="max-w-4xl mx-auto p-8">
        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-2 tracking-tight">
            Upload Resume
          </h2>
          <p className="text-gray-500">
            Upload resumes and provide a job description to start AI-powered
            screening.
          </p>
        </div>
        <div className="space-y-8">
          <div className="bg-white rounded-xl flex flex-col gap-4 p-6 shadow">
            <h2 className="text-lg font-semibold">1. Upload Resume</h2>
            <div className="relative border-2 border-dashed border-emerald-300 p-6 rounded-lg flex items-center justify-center hover:bg-emerald-50/30 flex-col py-18 cursor-pointer">
              <span className="bg-emerald-50 p-4 rounded-2xl">
                <Upload className="text-emerald-500 " size={35} />
              </span>
              <input
                id="file-upload"
                type="file"
                className="absolute w-full h-full opacity-0 border border-white py-2 px-4 rounded-lg hover:cursor-pointer"
                accept="application/pdf"
                onChange={(e) => setFile(e.target.files?.[0] || null)}
              />
              {!isUploaded && (
                <div className="flex flex-col gap-1 text-center mt-5">
                  <p className="text-lg font-medium">Drag & drop resume here</p>
                  <p className="text-sm text-gray-500">
                    or click to browse files
                  </p>
                  <p className="text-xs text-gray-500 mt-2">Max 5MB</p>
                </div>
              )}
            </div>
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
          </div>
          <div className="bg-white rounded-xl flex flex-col gap-4 p-6 shadow">
            <h2 className="text-lg font-semibold">2. Add Job Description</h2>
            <p className="text-gray-500 text-sm">
              Paste your job description to match candidates against specific
              requirements.
            </p>
            <textarea className="flex min-h-20 h-50 bg-gray-100 w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-emerald-500 focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 focus:bg-white" placeholder="Paste your job description here...      Example:   We are looking for a Senior Software Engineer with 5+ years of experience in React, TypeScript, and Node.js. The ideal candidate should have experience with cloud platforms (AWS/GCP), be comfortable with agile methodologies, and possess strong communication skills..." />
          </div>
        </div>
      </div>
    </main>
  );
};

export default UploadPage;
