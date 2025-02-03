import React from "react";

export const RightSidebar: React.FC = () => {
  return (
    <div className="h-screen bg-[#FFFDF9] p-4 border-l-1 border-black">
      <div className="mb-8">
        <h1 className="text-xl font-semibold text-gray-800">친구 & 채팅</h1>
      </div>

      <nav className="flex flex-col space-y-4">
        {/* 친구 목록 박스 */}
        <div className="border border-gray-200 rounded-lg p-4 min-h-[200px]">
          <h2 className="text-lg font-medium text-gray-700 mb-4">친구 목록</h2>
        </div>

        {/* 채팅 박스 */}
        <div className="border border-gray-200 rounded-lg p-4 min-h-[200px]">
          <h2 className="text-lg font-medium text-gray-700 mb-4">채팅</h2>
        </div>
      </nav>
    </div>
  );
};
