import { Sparkles } from "lucide-react";

const Navbar = () => {
  return (
    <div className="bg-white py-3 px-20 flex items-center justify-between">
      <div className="flex items-center gap-2">
        <Sparkles className="bg-emerald-400 text-white rounded-xl p-1.5" size={30}/>
        <h2 className="font-semibold text-xl">Resume AI</h2>
      </div>
      <div className="flex items-center justify-between gap-4 text-gray-600 text-sm font-medium">
        <a href="#" className="hover:text-black">Dashboard</a>
        <a href="#" className="hover:text-black">Upload</a>
        <a href="#" className="hover:text-black">Results</a>
      </div>
      <div className="flex items-center gap-4">
        <button className="text-gray-800 hover:bg-emerald-50 p-2 px-3 rounded-xl hover:text-emerald-600 text-sm cursor-pointer">
          Sign in
        </button>
        <button className="bg-emerald-500/90 p-2 px-3 text-white rounded-xl cursor-pointer">
          Get started
        </button>
      </div>
    </div>
  );
};

export default Navbar