/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */

export interface paths {
    "/api/calendars/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getCalendarById"];
        put: operations["updateCalendar"];
        post?: never;
        delete: operations["deleteCalendar"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/calendars/{calendarId}/schedules/{scheduleId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 특정 일정 조회
         * @description 주어진 일정 ID(scheduleId)를 기반으로 특정 일정을 조회합니다.
         */
        get: operations["getSchedule"];
        /**
         * 일정 수정
         * @description 기존 일정(scheduleId)을 수정합니다.
         */
        put: operations["updateSchedule"];
        post?: never;
        /**
         * 일정 삭제
         * @description 지정된 일정(scheduleId)을 삭제합니다.
         */
        delete: operations["deleteSchedule"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/user": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 내정보 수정 */
        post: operations["modifyUser"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/calendars": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getAllCalendars"];
        put?: never;
        post: operations["createCalendar"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/calendars/{calendarId}/schedules": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 일정 목록 조회
         * @description 지정된 날짜(date)에 해당하는 일정을 조회합니다.
         */
        get: operations["getSchedules"];
        put?: never;
        /**
         * 일정 생성
         * @description 주어진 calendarId에 새 일정을 생성합니다.
         */
        post: operations["createSchedule"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/verification-codes": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 인증번호 발송 */
        post: operations["sendVerification"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/verification-codes/verify": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 관리자 계정 잠김 해제 */
        post: operations["unlockAdminAccount"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 관리자 로그인 */
        post: operations["login"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/{username}/password": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        /** 관리자 비밀번호 변경 */
        patch: operations["changePassword"];
        trace?: never;
    };
    "/user/me": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 내 정보 */
        get: operations["me"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/calendars/{calendarId}/schedules/daily": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 하루 일정 조회
         * @description 특정 날짜(24시간)의 일정을 조회합니다.
         */
        get: operations["getDailySchedules"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 회원 명단 조회 (페이징, 검색) */
        get: operations["users"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/user/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        /** 회원 탈퇴 (soft) */
        delete: operations["deleteUser"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/logout": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        /** 로그아웃 */
        delete: operations["logout"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
}
export type webhooks = Record<string, never>;
export interface components {
    schemas: {
        Empty: Record<string, never>;
        RsDataEmpty: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["Empty"];
        };
        CalendarUpdateDto: {
            name?: string;
            description?: string;
        };
        Location: {
            /** Format: double */
            latitude?: number;
            /** Format: double */
            longitude?: number;
            address?: string;
        };
        ScheduleRequestDto: {
            title: string;
            description?: string;
            /** Format: date-time */
            startTime: string;
            /** Format: date-time */
            endTime: string;
            location?: components["schemas"]["Location"];
            endTimeValid?: boolean;
        };
        ScheduleResponseDto: {
            /** Format: int64 */
            id?: number;
            /** Format: int64 */
            calendarId?: number;
            title?: string;
            description?: string;
            /** Format: date-time */
            startTime?: string;
            /** Format: date-time */
            endTime?: string;
            location?: components["schemas"]["Location"];
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
        };
        modifyUserReqBody: {
            nickname: string;
        };
        RsDataVoid: {
            resultCode: string;
            msg: string;
            data: Record<string, never>;
        };
        CalendarCreateDto: {
            /** Format: int64 */
            userId?: number;
            name?: string;
            description?: string;
        };
        Calendar: {
            /** Format: int64 */
            id?: number;
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            user?: components["schemas"]["SiteUser"];
            name?: string;
            description?: string;
            sharedUsers?: components["schemas"]["SharedCalendar"][];
            messageList?: components["schemas"]["Message"][];
        };
        GrantedAuthority: {
            authority?: string;
        };
        Message: {
            /** Format: int64 */
            id?: number;
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            user?: components["schemas"]["SiteUser"];
            calendar?: components["schemas"]["Calendar"];
            content?: string;
            read?: boolean;
        };
        SharedCalendar: {
            /** Format: int64 */
            id?: number;
            calendar?: components["schemas"]["Calendar"];
            user?: components["schemas"]["SiteUser"];
        };
        SiteUser: {
            /** Format: int64 */
            id?: number;
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            username?: string;
            nickname?: string;
            password?: string;
            email?: string;
            /** @enum {string} */
            role?: "ADMIN" | "USER";
            apiKey?: string;
            /** Format: date-time */
            deletedDate?: string;
            locked?: boolean;
            deleted?: boolean;
            authorities?: components["schemas"]["GrantedAuthority"][];
        };
        VerificationCodeRequest: {
            username?: string;
            email?: string;
        };
        VerificationCodeVerifyRequest: {
            username?: string;
            verificationCode?: string;
        };
        UserLoginReqBody: {
            username?: string;
            password?: string;
        };
        LoginDto: {
            item?: components["schemas"]["UserDto"];
            apiKey?: string;
            accessToken?: string;
        };
        RsDataLoginDto: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["LoginDto"];
        };
        UserDto: {
            /** Format: int64 */
            id?: number;
            username?: string;
            nickname?: string;
            email?: string;
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
        };
        PasswordChangeRequest: {
            password?: string;
        };
        PageDtoUserDto: {
            /** Format: int32 */
            currentPageNumber: number;
            /** Format: int32 */
            pageSize: number;
            /** Format: int64 */
            totalPages: number;
            /** Format: int64 */
            totalItems: number;
            items: components["schemas"]["UserDto"][];
        };
        RsDataPageDtoUserDto: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["PageDtoUserDto"];
        };
        RsDataUserDto: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["UserDto"];
        };
    };
    responses: never;
    parameters: never;
    requestBodies: never;
    headers: never;
    pathItems: never;
}
export type $defs = Record<string, never>;
export interface operations {
    getCalendarById: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": unknown;
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    updateCalendar: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CalendarUpdateDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": unknown;
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    deleteCalendar: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": string;
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    getSchedule: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                calendarId: number;
                scheduleId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 일정 조회 성공 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 일정이 존재하지 않음 */
            404: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
        };
    };
    updateSchedule: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                calendarId: number;
                scheduleId: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ScheduleRequestDto"];
            };
        };
        responses: {
            /** @description 일정 수정 성공 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 일정이 존재하지 않음 */
            404: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
        };
    };
    deleteSchedule: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                calendarId: number;
                scheduleId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 일정 삭제 성공 */
            204: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 일정이 존재하지 않음 */
            404: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    modifyUser: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["modifyUserReqBody"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataVoid"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    getAllCalendars: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["Calendar"][];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    createCalendar: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CalendarCreateDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["Calendar"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    getSchedules: {
        parameters: {
            query: {
                startDate: string;
                endDate: string;
            };
            header?: never;
            path: {
                calendarId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 일정 목록 조회 성공 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"][];
                };
            };
            /** @description 잘못된 날짜 형식 */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"][];
                };
            };
        };
    };
    createSchedule: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                calendarId: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ScheduleRequestDto"];
            };
        };
        responses: {
            /** @description 일정 생성 성공 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
            /** @description 잘못된 요청 데이터 */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"];
                };
            };
        };
    };
    sendVerification: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["VerificationCodeRequest"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataVoid"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    unlockAdminAccount: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["VerificationCodeVerifyRequest"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    login: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["UserLoginReqBody"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataLoginDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    changePassword: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                username: string;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["PasswordChangeRequest"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataVoid"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    me: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["UserDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    getDailySchedules: {
        parameters: {
            query: {
                date: string;
            };
            header?: never;
            path: {
                calendarId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 하루 일정 조회 성공 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"][];
                };
            };
            /** @description 잘못된 날짜 형식 */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
            /** @description 서버 오류 */
            500: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["ScheduleResponseDto"][];
                };
            };
        };
    };
    users: {
        parameters: {
            query?: {
                page?: number;
                pageSize?: number;
                searchKeywordType?: string;
                searchKeyword?: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataPageDtoUserDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    deleteUser: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataUserDto"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
    logout: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataVoid"];
                };
            };
            /** @description Bad Request */
            400: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["RsDataEmpty"];
                };
            };
        };
    };
}
