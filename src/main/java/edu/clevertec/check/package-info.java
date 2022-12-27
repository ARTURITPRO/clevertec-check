/**
 * This project was developed for the test task of the clevertec company.
 * <pre>
 *  Launch :
 *  1.) gradle compile
 *  2.) gradle exec:java -D exec.mainClass=CheckRunner -D exec.args="1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111"
 *  or
 *  gradle exec:java -D exec.mainClass=CheckRunner -D exec.args="src\main\resources\data.txt"
 *  Running tests : gradle test
 *      If the path to the data file is passed as arguments, the data will be taken from it
 *  and the receipt will be written to the created file: "src/main/resources/receipt.txt".
 *      If the arguments are the data itself (format 1-3, etc., where 1-id 3-number), the check will be
 *  printed to console.
 * Passing goods and their number to arguments:
 *      The first digit of id, if the passed id exceeds the maximum allowable (9) an InvalidProductException will be
 * thrown.
 *      If an argument with id is passed that has already been passed in the same argument list, then their number is
 * summed up. For example "2-2 2-3 2-4" -> "2-9"
 *      Passing a map to arguments:
 * The card can be of the mastercard type. The discount, number, id for each card is listed in the discount_card
 * database.
 * The card name and number are first checked by the pattern. The name must be between 8 and 10 characters long.
 * The card number must be 5 digits. Otherwise, a SourceArgs exception will be thrown.
 *      Next, the name of the card is checked for a letter-by-letter match "masterCard" in any case. Otherwise
 *  an InvalidCardNameException will be thrown.
 *  Card number must be 5 digits, otherwise it will be thrown out InvalidCardNumberException.
 *  When passing a map as an argument, the type of map and its number are separated by a hyphen.
 *  Example result with arguments "1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111"
 *               Dionis
 *             EKE "Centrail"
 *             +375445581844
 *  ZWDN:304219               CKHO:300030394
 *  REGN:3100016076           UNP:390286042
 *  KASSA:0001 Change:000750  DKH:000271821
 *  31 Osipova Tatiana        CHK:01/000000285
 *  TIME  20:56:44            DATE  2022-12-20
 *  ----------------------------------------
 *  QTY   DESCRIPTION       PRICE      TOTAL
 *  1     milk              $1.0       $1
 *  2     brot              $1.5       $3
 *       The item pie is promotional
 *       Its amount is more than 5
 *       You get a 10% discount
 *       The cost pie will be:$4,5
 *       Instead of $5.0
 *  9    pie               $4,5       $40,5
 *  3    icecream          $2.0       $6
 *  8    popcorn           $4.5       $36
 *  6    pizza             $3.5       $21
 *       The item pudding is promotional
 *       Its amount is more than 5
 *     You get a 10% discount
 *     The cost pudding will be:$3,6
 *     Instead of $4.0
 *  7    pudding           $3,6       $25,2
 *  4    chocolate         $2.5       $10
 *     The item chicken is promotional
 *     Its amount is more than 5
 *     You get a 10% discount
 *     The cost chicken will be:$2,7
 *     Instead of $3.0
 *  5    chicken           $2,7       $13,5
 *  ----------------------------------------
 *  #	 DiscountCard goldCard number 55555
 *  #	 has been provided
 *  #	 Included 10% discount
 *  ----------------------------------------
 *  ####  Total cost:                 $140,58
 * </pre>
 *
 * @since 1.0
 * @author Artur Malashkov
 * @version JDK 1.8
 */
package edu.clevertec.check;
