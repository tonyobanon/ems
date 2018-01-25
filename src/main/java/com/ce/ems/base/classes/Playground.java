package com.ce.ems.base.classes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.ce.ems.base.api.event_streams.Preposition;
import com.ce.ems.base.classes.spec.ScoreGradeSpec;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.utils.Dates;
import com.ce.ems.utils.Utils;
import com.google.appengine.repackaged.org.joda.time.chrono.ZonedChronology;

public class Playground {

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		
		
		
		TimeZone.getTimeZone("PST").getRawOffset();
		
//		
//		System.out.println(validateGrades(grades));
		
		
//		String o = "WITH, AT, FROM, INTO, DURING, INCLUDING, UNTIL, AGAINST, AMONG, THROUGHOUT, DESPITE, TOWARDS, UPON, CONCERNING, OF, TO, IN, FOR, ON, BY, ABOUT, " + 
//				"LIKE, THROUGH, OVER, BEFORE, BETWEEN, AFTER, SINCE, WITHOUT, UNDER, WITHIN, ALONG, FOLLOWING, ACCROSS, BEHIND, BEYOND, PLUS, EXCEPT, BUT, UP, " + 
//				"OUT, AROUND, DOWN, OFF, ABOVE, NEAR";
//		
//		
//		
//		for(String i : o.split(", ")) {
//			System.out.println(i.toLowerCase() + "=" + i.toLowerCase().replace("_", " "));
//		}
//		
//		
		//System.out.println(Dates.toString(new Date()));
		
//		
		
		
//		System.out.println(Boolean.parseBoolean("undefined"));
		
//		Map<Preposition, String> v = new HashMap<>();
//		
//		v.put(Preposition.ABOUT, "1");
//		
//		
//		System.out.println(v.get(Preposition.ABOUT));
//		
		
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
	
	private static boolean validateGrades(List<ScoreGradeSpec> grades) {

		boolean b = true;

		// First verify length
		b = ScoreGrade.values().length == grades.size();
		if (!b) {
			return false;
		}

		// Then, verify that all scores are tentatively between 0 and 100
		b = grades.get(0).getLowerBound() == 0 && grades.get(grades.size() - 1).getUpperBound() == 100;
		if (!b) {
			return false;
		}

		// Then, verify the edges are numerically successive
		for (int i = 0; i < grades.size() - 1; i++) {
			b = grades.get(i).getUpperBound() == grades.get(i + 1).getLowerBound() - 1;
			if (!b) {
				return false;
			}
		}

		// Then verify that from n + 1 [bounds] < .... < n [bounds]
		List<Integer> vs  = new ArrayList<Integer>(grades.size() * 2);
		for (int i = 0; i < grades.size(); i++) {
			ScoreGradeSpec v = grades.get(i);
			vs.add(v.getLowerBound());
			vs.add(v.getUpperBound());
		}
		int c = -1;
		for(int i : vs) {
			if(c < i) {
				c = i;
			}else {
				return false;
			}
		}
		
		return b;
	}

	public static ArrayList<ArrayList<Integer>> permute(int[] num) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

		// start from an empty list
		result.add(new ArrayList<Integer>());

		for (int i = 0; i < num.length; i++) {
			// list of list in current iteration of the array num
			ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();

			for (ArrayList<Integer> l : result) {
				// # of locations to insert is largest index + 1
				for (int j = 0; j < l.size() + 1; j++) {
					// + add num[i] to different locations
					l.add(j, num[i]);

					ArrayList<Integer> temp = new ArrayList<Integer>(l);
					current.add(temp);

					// System.out.println(temp);

					// - remove num[i] add
					l.remove(j);
				}
			}

			result = new ArrayList<ArrayList<Integer>>(current);
		}

		return result;
	}

}
