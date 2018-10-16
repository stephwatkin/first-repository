//Project 2 by Stephanie Watkins

import java.util.Scanner;
public class RleProgram
{
    //Main method. Here the menu will be loaded giving the user has the option to input flat data, RLE data, or hex data so that a pixel picture can be displayed.
    public static void main(String[] args)
    {
        int choice = 1;
        String myEntry;
        //"myEntryByte" represents an encoded byte and "imageByte" represents the decoded byte.
        byte[] myEntryByte = new byte[0];
        byte[] imageByte = new byte[0];
        //this boolean will determine whether a message of "no data" will be displayed or if the actual string will be displayed.
        boolean check = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the RLE image encoder!\n");
        System.out.println("Displaying Spectrum Image: ");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);

        //This loop allows the menu to keep displaying after a choice is selected. Once the user selects 0 the program ends.
        while (choice != 0)
        {
            //Displays the menu and prompts the user to select an option.
            System.out.println("\nRLE Menu");
            System.out.println("--------");
            System.out.println("0. Exit\n1. Load File\n2. Load Test Image\n3. Read RLE String\n4. Read RLE Hex String\n5. Read Data Hex String\n6. Display Image");
            System.out.println("7. Display RLE String\n8. Display Hex RLE Data\n9. Display Hex Flat Data\n");
            System.out.print("Select a Menu Option:");
            choice = input.nextInt();

            //When the user selects 1, they are prompted to load a file. This file comes from a file stored on the computer.
            if (choice == 1)
            {
                System.out.print("Enter name of file to load: ");
                myEntry = input.next();
                imageByte = ConsoleGfx.loadFile(myEntry);
                check = true;
            }

            //When the user selects 2, the test image is loaded.
            else if (choice == 2)
            {
                System.out.println("Test image data loaded.");
                imageByte = ConsoleGfx.testImage;
                check = true;
            }

            //When the user selects 3, the user inputs a string of decimal notation with delimiters to be converted to RLE data.
            else if (choice == 3)
            {
                System.out.print("Enter an RLE string to be decoded: ");
                myEntry = input.next();
                myEntryByte = stringToRle(myEntry);
                imageByte = decodeRle(myEntryByte);
                check = true;
            }

            //When the user selects 4, the user inputs a hexadecimal string with no delimiters to be converted to RLE data.
            else if (choice == 4)
            {
                System.out.print("Enter the hex string holding RLE data: ");
                myEntry = input.next();
                myEntryByte = stringToData(myEntry);
                imageByte = decodeRle(myEntryByte);
                check = true;
            }

            //When the user selects 5, the user inputs a hex string to be read in raw flat data.
            else if (choice == 5)
            {
                System.out.print("Enter the hex string holding flat data: ");
                myEntry = input.next();
                imageByte = stringToFlat(myEntry);
                check = true;
            }

            //When the user selects 6, the image corresponding with the converted string is displayed.
            else if (choice == 6)
            {
                System.out.println("Displaying image...");

                //If no data was inputted then a "no data" message will be displayed. If there was data inputted the image is displayed.
                if (!check)
                {
                    System.out.println("No Data");
                }
                else
                {
                    ConsoleGfx.displayImage(imageByte);
                }
            }

            //When the user selects 7, the current data is converted into human readable RLE representation with delimiters.
            else if (choice == 7)
            {
                System.out.print("RLE representation: ");
                if (!check)
                {
                    System.out.println("No Data");
                }
                else
                {
                    myEntryByte = encodeRle(imageByte);
                    System.out.println(toRleString(myEntryByte));
                }
            }

            //When the user selects 8, the current data is converted to RLE hexadecimal representation without delimiters.
            else if (choice == 8)
            {
                System.out.print("RLE hex values: ");
                if (!check) {
                    System.out.println("No Data");
                } else {
                    myEntryByte = encodeRle(imageByte);
                    System.out.println(toHexString(myEntryByte));
                }
            }

            //When the user selects 9, the current raw flat data in hexadecimal representation without delimiters.
            else if (choice == 9)
            {
                System.out.print("Flat hex values: ");
                if (!check)
                {
                    System.out.println("No Data");
                }
                else
                {
                    System.out.print(flatToString(imageByte));
                }
            }

            //If the user inputs a number not in the menu an error message is displayed.
            else if (choice != 0)
            {
                System.out.println("Error! Invalid input.");
            }
        }
    }

    //Translates data (RLE or raw) a hexadecimal string (without delimiters).
    public static String toHexString(byte[] data)
    {
        String myString = "";
        //A loop to add a hexadecimal from a byte array to a string.
        for (int i = 0; i < data.length; ++i)
        {
            myString += decToHex(data[i]);
        }
        return myString;
    }

    //Returns number of runs of data in an image data set; double this result for length of encoded (RLE) byte array.
    public static int countRuns(byte[] flatData)
    {
        int myCount = 1, numCount = 1;
        for (int i = 0; i < flatData.length - 1; ++i)
        {
            //If two numbers are compared and they are not the same the count increments, representing the amount of integers.
            if (flatData[i] != flatData[i + 1] || numCount == 15)
            {
                myCount++;
                numCount = 1;
            }
            else
            //numCounts are incremented to keep the number of bytes in the array below 16.
            {
                numCount++;
            }
        }
        return myCount;
    }

    //Returns encoding (in RLE) of the raw data passed in; used to generate RLE representation of a data
    public static byte[] encodeRle(byte[] flatData)
    {
        int dataCount = 1;
        int j = 0;
        //Calls upon countRuns to get the length of the new byte myRleData.
        byte[] myRleData = new byte[(countRuns(flatData)) * 2];

        for (int i = 1; i <= flatData.length; i++)
        {
            if (i != flatData.length)
            {
                //If the two numbers compared are equal, the count of the amount of numbers repeated(dataCount) increments.
                if (flatData[i - 1] == flatData[i] && dataCount < 15)
                {
                    dataCount++;
                }
                //If the two numbers are not equal the data count and the number that was repeated is added to the new byte.
                else
                {
                    myRleData[j] = (byte) dataCount;
                    j++;
                    myRleData[j] = flatData[i - 1];
                    j++;
                    dataCount = 1;
                }
            }

            //If i is the last integer, no matter what the last two numbers will be added to the byte.
            else
            {
                myRleData[j] = (byte) dataCount;
                j++;
                myRleData[j] = flatData[i - 1];
                j++;
            }
        }
        return myRleData;
    }

    //Returns decompressed size RLE data; used to generate flat data from RLE encoding.
    public static int getDecodedLength(byte[] rleData)
{
    int myDecodeLength = 0;
    int i = 0;
    for (i=0; i <rleData.length; i = i +2)
    {
        myDecodeLength += rleData[i];
    }
//    while (i < rleData.length)
//    {
//        //If the number in the byte is represented by an even index, those number in those spots will be added to calculate the total amount of elements in the array.
////            if (i % 2 == 0)
////            {
////                myDecodeLength += rleData[i];
////            }
////            i++;
//        for (i=0; i <rleData.length; i = i +2)
//        {
//            myDecodeLength += rleData[i];
//        }
//    }
     return myDecodeLength;
}

    //Returns the decoded data set from RLE encoded data. This decompresses RLE data for use.
    public static byte[] decodeRle(byte[] rleData)
    {
        //Calls upon the getDecodeLength so the length of the new byte can be initiated.
        byte[] decodedByte = new byte[getDecodedLength(rleData)];
        int myCount =0, j = 0;
        for (int i = 0; i < rleData.length; i = i +2)
        {
            //While the count does not equal the number representing the count of integers repeated, the count increments and the number at the next index is added to the new byte.
            while (myCount != rleData[i])
            {
                myCount++;
                decodedByte[j] = rleData[i + 1];
                j++;
            }
            myCount = 0;
        }
        return decodedByte;
    }

    //Translates a string in hexadecimal format into byte data (can be raw or RLE).
    public static byte[] stringToData(String dataString)
    {
        int j = 0;
        byte[] myDataByte = new byte[dataString.length()];
        //Calls on the charDecode method to convert the hexadecimal numbers and then add them to a new byte.
        for (int i = 0; i < dataString.length(); ++i)
        {
            myDataByte[j] = charDecode(dataString.charAt(i));
            j++;
        }
        return myDataByte;
    }

    //Translates RLE data into a human-readable representation. For each run, in order, it should display the run length in decimal (1-2 digits); the run value in hexadecimal (1 digit); and a delimiter, ‘:’, between runs.
    public static String toRleString(byte[] rleData)
    {
        String rleString = "";
        for (int i = 0; i < rleData.length; ++i)
        {
            //If the index is an even number the dat at that index is added to the string. Else, the data is converted from decimal to hexadecimal and then added to the string along with a delimiter.
            if (i % 2 == 0)
            {
                rleString += rleData[i];
            }
            else
            {
                rleString += decToHex(rleData[i]) + ":";
            }
        }
        return rleString.substring(0, rleString.length() - 1);
    }

    //Translates a string in human-readable RLE format (with delimiters) into RLE byte data.
    public static byte[] stringToRle(String rleString)
    {
        int myIncrementor = 0, j = 0, count = 0;
        String stringForRle = "";

        for (int i = 0; i < rleString.length(); i++)
        {
            //If there is a delimiter the count increments so that we can find the length of the new byte.
            if (rleString.charAt(i) == ':')
            {
                count++;
            }
        }

        byte[] rleData = new byte[(count + 1) * 2];
        rleString += ':';

        for (int y = 0; y < rleString.length(); y++)
        {
            //if the character is a delimiter, the numbers in front will need to be added to the byte.
            if (rleString.charAt(y) == ':')
            {
                stringForRle = rleString.substring(myIncrementor, y);
                //If there are three numbers in front of the :, that means the first number is a double digit number. The third character will need to be converted from hexadecimal.
                if (stringForRle.length() == 3)
                {
                    rleData[j] = (byte) ((((int) stringForRle.charAt(0) - 48) * 10) + (stringForRle.charAt(1) - 48));
                    j++;
                    rleData[j] = (charDecode(stringForRle.charAt(2)));
                    j++;
                }
                //If there are two numbers the first is the decimal number and the second need to be converted from hexadecimal.
                else if (stringForRle.length() == 2)
                {
                    rleData[j] = (byte) ((byte) stringForRle.charAt(0) - 48);
                    j++;
                    rleData[j] = (charDecode(stringForRle.charAt(1)));
                    j++;
                }
                myIncrementor = y + 1;
            }
        }
        return rleData;
    }

    //Modified from a method created in lab 4
    public static byte charDecode(char digit)
    {
        byte result;

        //will convert the hexadecimal letters to their corresponding numbers.
        if (digit == 'F' || digit == 'f')
            result = 15;
        else if (digit == 'E' || digit == 'e')
            result = 14;
        else if (digit == 'D' || digit == 'd')
            result = 13;
        else if (digit == 'C' || digit == 'c')
            result = 12;
        else if (digit == 'B' || digit == 'b')
            result = 11;
        else if (digit == 'A' || digit == 'a')
            result = 10;
        else
            result = (byte) ((byte) digit - 48);

        return result;
    }

    //Used for choice selection 5. Converts the string from hexadecimal to flat.
    public static byte[] stringToFlat(String flatString)
    {
        byte[] rawByte = new byte[flatString.length()];
        for (int i = 0; i < flatString.length(); ++i)
        {
            rawByte[i] = (charDecode(flatString.charAt(i)));
        }
        return rawByte;
    }

    //Created to aid in fulfilling choice 9
    public static String flatToString(byte[] flatData)
    {
        String myFlatString = "";
        //Converts the decimal numbers in the byte to hexadecimal
        for (int i = 0; i <flatData.length; ++i)
        {
            myFlatString += decToHex(flatData[i]);
        }
        return myFlatString;
    }

    //Created to help convert decimal numbers to hexadecimal.
    public static char decToHex(byte myNumber)
    {
        //Converts the decimal numbers to their equivalent hexadecimal letters.
        char byteDecode;
        switch (myNumber) {
            case 10:
                byteDecode = 'a';
                break;
            case 11:
                byteDecode = 'b';
                break;
            case 12:
                byteDecode = 'c';
                break;
            case 13:
                byteDecode = 'd';
                break;
            case 14:
                byteDecode = 'e';
                break;
            case 15:
                byteDecode = 'f';
                break;
            default:
                byteDecode = (char) (myNumber + 48);
                break;
        }
        return byteDecode;
    }
}