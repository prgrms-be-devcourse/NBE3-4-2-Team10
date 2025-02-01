/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */

export interface paths {
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
        UserLoginReqBody: {
            username?: string;
            password?: string;
        };
        RsDataUserLoginResBody: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["UserLoginResBody"];
        };
        UserDto: {
            /** Format: int64 */
            id?: number;
            username?: string;
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            email?: string;
        };
        UserLoginResBody: {
            item?: components["schemas"]["UserDto"];
            apiKey?: string;
            accessToken?: string;
        };
        PageDtoUserDto: {
            /** Format: int32 */
            currentPageNumber?: number;
            /** Format: int32 */
            pageSize?: number;
            /** Format: int64 */
            totalPages?: number;
            /** Format: int64 */
            totalItems?: number;
            items?: components["schemas"]["UserDto"][];
        };
        RsDataPageDtoUserDto: {
            resultCode: string;
            msg: string;
            data: components["schemas"]["PageDtoUserDto"];
        };
        RsDataVoid: {
            resultCode: string;
            msg: string;
            data: Record<string, never>;
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
                    "application/json;charset=UTF-8": components["schemas"]["RsDataUserLoginResBody"];
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
