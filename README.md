# clevertec-check

## Данное приложение использует следующий стек технологий:

| Стек технологий         | Название                                      |
|-------------------------|-----------------------------------------------|
| Java                    | 17                                            |
| Система контроля версий | Git                                           |    
| Удалённый репозиторий   | https://github.com/ARTURITPRO/clevertec-check |
| Database                | PostgreSQL                                    |
 | Сборщик                 | Gradle                                        |
| Плагин                  | Lombook                                       |
| Логирование             | Slf4j                                         |   
| Печать                  | В консоль, txt файл, PDF файл                 |
| Тесты                   | junit.jupiter:api:5                           |

## Перед запуском приложения, нужно выполнить инструкции в файлах:  
```sh
  resources/jdbc/test.sql и resources/jdbc/data.sql 
```
  ## Способы запуска
 ### 1.) Запуск через командную строку
```sh
  gradle exec:java -D exec.mainClass=CheckRunner -D exec.args= "1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111"
```
 ### 2.) Запуск через командную строку   
```sh 
  gradle exec:java -D exec.mainClass=CheckRunner -D exec.args="src\main\resources\data.txt"
```
 # Способы вывода
- Если в качестве аргументов передается путь к файлу данных, данные будут взяты из него и чек будет записан в  файл: 
 ```sh   
 src/main/resources/receipt.txt
 ``` 
 - Если аргументами являются сами данные (формат 1-3 и т.д., где 1-id 3-число), то проверка будет выводится на консоль.
 - Также приложение всегда записывает вывод чека в корень приложения в pdf формате. Имя файла:  "checkOfSupermarket.pdf"
# Логирование
  Настройка логирования производится в файле: 
 ```sh  
resources/logback.xml
 ```
  Вывод логов по умолчанию производится в файл:   
```sh 
logs/trace.log
```  
# Документирование
   Генерация JavaDoc по вашему усмотрению.
 ## Передача продуктов и их количества в аргументы:   
  Первая цифра - идентификатор товара. Если переданный идентификатор превышает максимально допустимый, то будет
  выброшено исключение InvalidProductException. Если передается аргумент с id, который уже был передан в этом же списке 
  аргументов, то их количество ссумируется. Например "2-2 2-3 2-4" -> "2-9"
 ## Передача карты в аргументы:
 Карта может иметь название только  mastercard без учета регистра. Скидка, номер, ид  для каждой карты указана в базе 
 данных discount_card. Название и номер карты сначала проверяются по шаблону.  Номер  карты должен состоять из 5 цифр, 
 в противном случае будет выдано исключение SourceArgs. Далее название карты  проверяется на  побуквенное совпадение 
 «masterCard». В противном случае будет выброшено исключение  InvalidCardNameException.  При передаче карты в качестве 
 аргумента тип карты и ее номер разделяются дефисом.
 # Пример результата с аргументами:
```sh  
 1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111
```
<pre>
****************************************
*               Dionis                 *
*           EKE "Centrail"             *
*              +375445581844           *
ZWDN:304219              CKHO:300030394
REGN:3100016076           UNP:390286042
KASSA:0001 Change:000750  DKH:000271821
31 Osipova Tatiana     CHK:01/000000285
TIME  18:18:58		   DATE  2022-12-20
----------------------------------------
QTY   DESCRIPTION       PRICE      TOTAL
*     The item chicken is promotional
*     Its amount is more than 5
*     You get a 10% discount 
*     The cost chicken will be:$2,7
*     Instead of $3.0        
5     chicken           $2,7       $13,5     
1     milk              $1.0       $1        
2     brot              $1.5       $3        
6     pizza             $3.5       $21       
*     The item pie is promotional
*     Its amount is more than 5
*     You get a 10% discount 
*     The cost pie will be:$4,5
*     Instead of $5.0        
9     pie               $4,5       $40,5     
8     popcorn           $4.5       $36       
*     The item pudding is promotional
*     Its amount is more than 5
*     You get a 10% discount 
*     The cost pudding will be:$3,6
*     Instead of $4.0        
7     pudding           $3,6       $25,2     
4     chocolate         $2.5       $10       
3     icecream          $2.0       $6        
----------------------------------------
#	  DiscountCard mastercard number 11111
#	  has been provided
#	  Included 10% discount
----------------------------------------
####  Total cost:                 $140,58
</pre>