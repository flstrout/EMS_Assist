package com.fstrout.emsassist;

/**
 * Created by fstrout on 11/20/17.
 *
 */

public class BarcodeTypes {
    /**
     * Barcode Types Constants
     */
    private static final String CODE_128 = "STANDARD:CODE 128";
    private static final String CODE_39 = "STANDARD:CODE 39";
    private static final String CODE_93 = "STANDARD:CODE 93";
    private static final String CODABAR = "STANDARD:CODABAR";
    private static final String DATA_MATRIX = "2D:DATA MATRIX";
    private static final String EAN_13 = "STANDARD:EAN 13";
    private static final String EAN_8 = "STANDARD:EAN 8";
    private static final String ITF = "STANDARD:ITF 14";
    private static final String QR_CODE = "2D:QR CODE";
    private static final String UPC_A = "STANDARD:UPC A";
    private static final String UPC_E = "STANDARD:UPC E";
    private static final String PDF_417 = "2D:PDF 417";
    private static final String AZTEC = "2D:AZTEC";

    static public String toString(int codeTypeInt) {
        String codeTypeString;

        switch (codeTypeInt) {
            case 1:
                codeTypeString = BarcodeTypes.CODE_128;
                break;

            case 2:
                codeTypeString = BarcodeTypes.CODE_39;
                break;

            case 4:
                codeTypeString = BarcodeTypes.CODE_93;
                break;

            case 8:
                codeTypeString = BarcodeTypes.CODABAR;
                break;

            case 16:
                codeTypeString = BarcodeTypes.DATA_MATRIX;
                break;

            case 32:
                codeTypeString = BarcodeTypes.EAN_13;
                break;

            case 64:
                codeTypeString = BarcodeTypes.EAN_8;
                break;

            case 128:
                codeTypeString = BarcodeTypes.ITF;
                break;

            case 256:
                codeTypeString = BarcodeTypes.QR_CODE;
                break;

            case 512:
                codeTypeString = BarcodeTypes.UPC_A;
                break;

            case 1024:
                codeTypeString = BarcodeTypes.UPC_E;
                break;

            case 2048:
                codeTypeString = BarcodeTypes.PDF_417;
                break;

            case 4096:
                codeTypeString = BarcodeTypes.AZTEC;
                break;

            default:
                codeTypeString = BarcodeTypes.CODE_128;
                break;
        }
        return codeTypeString;
    }
}
