import { Sparkles } from "lucide-react";
import Link from "next/link";

export default function Home() {
  return (
    <main className="h-screen flex items-center justify-center bg-emerald-50/70">
      <div className="flex flex-col justify-center items-center gap-8 max-w-4xl mx-auto text-center">
        <div className="flex items-center justify-center gap-2 rounded-2xl border border-emerald-500 px-4 py-1 text-sm">
          <Sparkles className="mx-auto text-green-500" size={15} />
          <p className="text-emerald-600">AI-Powrered Resume Screening</p>
        </div>
        <h1 className="text-4xl md:text-4xl lg:text-6xl font-bold tracking-light">
          <span className="mr-4">Screen</span>
          <span className="text-emerald-500">1,000 Resumes</span>
          <span className="ml-4">in seconds using AI</span>
        </h1>
        <p className="text-xl text-gray-500 px-30">
          Upload resumes, paste your job description, and let AI instantly
          parse, score, and shortlist the best candidates. Save hours of manual
          screening.
        </p>
        <Link className="bg-emerald-400 text-white px-4 py-4 rounded-xl text-lg cursor-pointer" href="/upload">
          Upload Resumes
        </Link>
      </div>
    </main>
  );
}
