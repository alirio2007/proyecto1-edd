/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author
 */
public class Cola<T> {
    private NodoCola<T> pFirst;
    private NodoCola<T> pLast;
    private int iN;

    /**
     * Constructor
     */
    public Cola() {
        pFirst = null;
        pLast = null;
        iN = 0;

    }
}
