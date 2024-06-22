# Вход
### символ 1
	либо символ c (3-я буква латинского алфавита), либо d -- направление работы архиватора
### символы со 2-го до конца
	данные для сжатия или данные, сжатые Вашей программой ранее
	данные могут содержать символы с произвольными кодами от 0 до 255

# Выход
	если 1-й символ на входе -- это c, то сжатые данные
	если 1-й символ на входе -- это d, то расжатые данные

# Ограничения
Исполняемый файл + данные не более 4Mb

# Пример 1
### Вход
<pre>
c0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
</pre>
### Выход
какие-то двоичные данные, зависящие от Вашей реализации


# Пример 2
### Вход
символ d и затем выход для Примера 1
### Выход
<pre>
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
0123456789ABCD
</pre>