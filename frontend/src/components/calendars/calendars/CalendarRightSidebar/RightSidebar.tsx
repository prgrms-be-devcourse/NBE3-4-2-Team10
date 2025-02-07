import React from "react";

export const RightSidebar: React.FC = () => {
  return (
    <div className="h-screen bg-white p-4 border-r border-gray-200">
      <div className="mb-8 text-center"> {/* 가운데 정렬 추가 */}
        <h1 className="text-lg font-thin text-gray-800">COMMUNITY</h1>
      </div>

      <nav className="flex flex-col space-y-4">
        {/* 친구 목록 박스 */}
        <div className="border border-gray-200 rounded-lg p-4 min-h-[200px]">
          <h2 className="text-xs font-extralight text-gray-600 mb-4">FRIENDS</h2>
        </div>

        {/* 채팅 박스 */}
        <div className="border border-gray-200 rounded-lg p-4 min-h-[200px]">
          <h2 className="text-xs font-extralight text-gray-600 mb-4">CHAT</h2>
        </div>
      </nav>
    </div>
  );
};