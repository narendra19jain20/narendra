package firstframe;
/**
 * 
 */

/**
 * @author 0000100363
 *
 */
public class pattern_matching {

	public pattern_matching() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	static int C = 0;
	static int INDEX = -1;

	public boolean SubString(String A, String B) {
		// String[] X = new String[A.length()];
		// String[] Y = new String[B.length()];
		int MAX = A.length() - B.length() + 1;
		for (int i = 0; i < MAX; i++) {
			int j;
			for (j = 0; j < B.length(); j++) {
				C++;
				if (B.charAt(j) == A.charAt(i + j))
					continue;
				else
					break;
			}
			if (j == B.length()) {
				INDEX = i - j;
				return true;
			}
		}
		return false;
	}

	/***************************
	 * int Freq=0; 
	 * public boolean SubString(String A, String B) { 
	 * // String[] X = new String[A.length()]; // String[] Y = new String[B.length()]; int MAX
	 * = A.length() - B.length() + 1; for (int i = 0; i < MAX; i++) { int j; for
	 * (j=0; j < B.length(); j++) { C++; if (B.charAt(j) == A.charAt(i+j))
	 * continue; else break; } if (j== B.length()){ Freq++; // i = i+j-1; //
	 * can't do this because if the string A is "aaaaaaaaaaaaa" and string B is
	 * "aa", this increment is false. } } if(Freq >0){ return true;
	 * System.out.println("string B appears " + Freq + " time in string A); }
	 * else return false; }
	 ****************************/

	// faster algo with table, stat diagramm wala funda.............. linear
	// order of comparions

	public boolean SubString_fast(String A, String B) {
		// String[] X = new String[A.length()];
		// String[] Y = new String[B.length()];
		int MAX = A.length() - B.length() + 1;

		return false;
	}

	// //

	public String replace_string(String A, String B, String C) {
		String X = null;
		int replace_count = 1;
		A = A.replaceFirst(B, C);
		// System.out.println("string A now is: " + A);
		while (SubString(A, B)) {
			A = A.replaceFirst(B, C);
			replace_count++;
		}
		System.out.println("Number of replacements made: " + replace_count);
		return A;
	}

	/*******************************
	 * public String replace_string(String A, String B, String C) { String X =
	 * null; A = A.replace(B,C); // replace all B with C return A; }
	 ******************************/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		pattern_matching p = new pattern_matching();
		if (p.SubString(args[0], args[1])) {
			System.out.println("B is a substring of A");
			if (args.length > 2) {
				String X = p.replace_string(args[0], args[1], args[2]);
				System.out.println("String A after replacement is: " + X);
			} else
				System.out.println("no replacement String provided......");
		} else
			System.out.println("B is not a substring of A");
		System.out.println("number of comparisons: " + C);
	}
}
