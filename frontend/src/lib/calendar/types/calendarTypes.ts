export interface Calendar {
  id: number;
  name: string;
  description: string;
  createDate: string;
  modifyDate: string;
}

export interface CalendarCreateDto {
  name: string;
  description: string;
}

export interface CalendarUpdateDto {
  name: string;
  description: string;
}
