"use client";

import axios from "axios";
import { useEffect, useState } from "react";

type Job = {
  id: string;
  title: string;
  description: string;
  createdAt: string;
};

const JobsListPage = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
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
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, []);

  if (loading) {
    return (
      <main className="h-screen flex items-center justify-center">
        <p>Loading jobs...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="h-screen flex items-center justify-center text-red-500">
        {error}
      </main>
    );
  }

  return (
    <main className="min-h-screen px-6 py-10">
      <h1 className="text-3xl font-bold mb-6">Available Jobs 🧾</h1>

      {jobs.length === 0 ? (
        <p>No jobs found.</p>
      ) : (
        <ul className="space-y-4">
          {jobs.map((job) => (
            <li
              key={job.id}
              className="border rounded p-4 hover:bg-gray-50 hover:text-gray-700"
            >
              <h2 className="text-xl font-semibold">{job.title}</h2>
              <p className="text-gray-600 mt-2">
                {job.description}
              </p>
              <p className="text-sm text-gray-400 mt-2">
                Created at: {new Date(job.createdAt).toLocaleString()}
              </p>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}

export default JobsListPage;