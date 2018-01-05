package com.ce.ems.base.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.ce.ems.base.classes.activitystreams.Preposition;
import com.ce.ems.utils.Utils;

public class Playground {

	public static void main(String[] args) {
		
		
		
		Map<Preposition, String> v = new HashMap<>();
		
		v.put(Preposition.ABOUT, "1");
		
		
		System.out.println(v.get(Preposition.ABOUT));
		
		
//		Locale l = Locale.forLanguageTag("en-US");
//	
//		System.out.println(l.getDisplayName());
//		System.out.println(l.getDisplayScript());
//		System.out.println(l.getISO3Language() + "-" + l.getISO3Country());
//		
		
//		int duration = 5;
//		
//		for (int i = 100; i < ((duration * 100) + 1 ); i += 100) {
//			System.out.println(i);
//		}
		
		
		
		
//		permute(new int[]{1, 2, 3}).forEach(l1 -> {
//			l1.forEach(i -> {
//				System.out.print(i + " ");
//			});
//			System.out.println("");
//		});
		
		
		
		
//		int pageSize = 60;
//		
//		Integer keysSize = 121;
//		
//		Integer pageCount = null;
//		
//		if (keysSize <= pageSize) {
//			pageCount = 1;
//		} else {
//			pageCount = keysSize / pageSize;
//			
//			if(keysSize % pageSize > 0) {
//				pageCount += 1;
//			}
//		}
//		
//		
//		System.out.println(pageCount);

	}
	
	
	public static ArrayList<ArrayList<Integer>> permute(int[] num) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
	 
		//start from an empty list
		result.add(new ArrayList<Integer>());
	 
		for (int i = 0; i < num.length; i++) {
			//list of list in current iteration of the array num
			ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();
	 
			for (ArrayList<Integer> l : result) {
				// # of locations to insert is largest index + 1
				for (int j = 0; j < l.size()+1; j++) {
					// + add num[i] to different locations
					l.add(j, num[i]);
	 
					ArrayList<Integer> temp = new ArrayList<Integer>(l);
					current.add(temp);
	 
					//System.out.println(temp);
	 
					// - remove num[i] add
					l.remove(j);
				}
			}
	 
			result = new ArrayList<ArrayList<Integer>>(current);
		}
	 
		return result;
	}

}
