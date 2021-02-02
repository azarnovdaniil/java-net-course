package com.geekbrains.dbox.WorkFiles.Data_File;

public enum WorkFileLinesMod {
    NEW, DOWN, ALL, UP, RANGE, NEXT
    /*
    Запись в файл
        NEW - переписать файл
        DOWN - дописать в файл
    Чтение из файла
     ALL - Все содержимое в массив
     UP - первоя строка(с начала)
     RANGE - диапазон считываемых строк strUp, strDown
     NEXT - следующая строка strEnd \ null при окончании файла
     */
}
