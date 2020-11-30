package com;

import java.util.Arrays;

public class MyTest {

    public static void main(String[] args) {

        int[][] arrs = new int[3][3];
        arrs[0] = new int[]{1,1,0};
        arrs[1] = new int[]{1,0,1};
        arrs[1] = new int[]{0,0,1};
//        arrs[1][1] = 10;
        System.out.println("qiao Arrays.toString(arrs) = " + Arrays.deepToString(arrs));
    }

}


//1,2,3,4
//5,6,7,8
//9,10,11,12

//
//    [[1, 1, 0],
//    [0, 0, 1],
//    [0, 0, 0]]
//[[1, 2, 3],
//  [2, 3, 4]]
