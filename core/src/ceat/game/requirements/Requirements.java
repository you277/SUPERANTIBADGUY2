package ceat.game.requirements;

import java.util.ArrayList;

public class Requirements {

    public static int[] intSelectionSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            int me = nums[i];
            int min = me;
            int lowestIdx = i;
            for (int v = i + 1; v < nums.length; v++) {
                int maybeMin = nums[v];
                if (maybeMin < min) {
                    min = maybeMin;
                    lowestIdx = v;
                }
            }
            nums[i] = min;
            nums[lowestIdx] = me;
        }
        return nums;
    }
    public static int[] intInsertionSort(int[] strs) {
        for(int i = 1; i < strs.length; i++) {
            int hi = strs[i];
            int v = i;
            while(v > 0 && hi < strs[v - 1]) {
                strs[v] = strs[v - 1];
                strs[v - 1] = hi;
                v--;
            }
        }
        return  strs;
    }
    public Requirements() {
        ArrayList<Integer> guy = new ArrayList<>();
        guy.add(0, 1);
        guy.set(0, 2);
        intSelectionSort(new int[] {1, 0, -1});
        intInsertionSort(new int[] {1, 0, -1});
    }
}
