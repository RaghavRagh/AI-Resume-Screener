"use client";

import axios from "axios";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";

type Job = {
  id: string;
  title: string;
};

type MatchResult = {
  matchScore: number;
  confidence: string;
  matchedSkills: string[];
  missingSkills: string[];
};

export default function MatchPage() {
  const searchParams = useSearchParams();
  const resumeId = searchParams.get("resumeId");

  const [jobs, setJobs] = useState<Job[]>([]);
  const [selectedJobId, setSelectedJobId] = useState("");
  const [result, setResult] = useState<MatchResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const res = await axios.get(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/jobs`
        );
        setJobs(res.data);
      } catch (err) {
        console.error(err);
        setError("Failed to load jobs");
      }
    };

    fetchJobs();
  }, []);

  const handleMatch = async () => {
    if (!resumeId || !selectedJobId) {
      setError("Resume or Job missing");
      return;
    }

    setLoading(true);
    setError("");
    setResult(null);

    try {
      const res = await axios.get(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/match`,
        {
          params: {
            resumeId,
            jobId: selectedJobId,
          },
        }
      );

      setResult(res.data);
    } catch (err) {
      console.error(err);
      setError("Matching failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen px-6 py-10 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Resume Matcher 🧠</h1>

      <p className="text-sm text-gray-500 mb-4">Resume ID: {resumeId}</p>

      {/* Job selector */}
      <select
        value={selectedJobId}
        onChange={(e) => setSelectedJobId(e.target.value)}
        className="w-full border px-4 py-2 rounded mb-4"
      >
        <option value="">Select a job</option>
        {jobs.map((job) => (
          <option key={job.id} value={job.id} className="text-black">
            {job.title}
          </option>
        ))}
      </select>

      <button
        onClick={handleMatch}
        disabled={loading}
        className={`px-6 py-2 rounded text-white ${
          loading ? "bg-gray-400" : "bg-black"
        }`}
      >
        {loading ? "Matching..." : "Match Resume"}
      </button>

      {error && <p className="text-red-500 mt-4">{error}</p>}

      {/* Result */}
      {result && (
        <div className="mt-8 border rounded p-4">
          <h2 className="text-xl font-semibold mb-2">
            Match Score: {result.matchScore}%
          </h2>

          <div className="mb-4">
            <h3 className="font-medium">Matched Skills ✅</h3>
            <ul className="list-disc list-inside">
              {result.matchedSkills.map((skill) => (
                <li key={skill}>{skill}</li>
              ))}
            </ul>
          </div>

          <div>
            <h3 className="font-medium">Missing Skills ❌</h3>
            <ul className="list-disc list-inside">
              {result.missingSkills.map((skill) => (
                <li key={skill}>{skill}</li>
              ))}
            </ul>
          </div>

          <h2 className="text-xl font-semibold">
            Match Score: {result.matchScore}% — {result.confidence}
          </h2>
        </div>
      )}
    </main>
  );
}
