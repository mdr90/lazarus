package com.android.lazarus.helpers;

public class MessageSplitter {

	final static String[] REG_EXPS = { "\\.\\.", "\\.", "\\;\\;", "\\;",
			"\\,\\,", "\\,", "\\:\\:", "\\:", "\\ \\ ", "\\ " };

	public static String[] splitMessage(String message, int maximumSize,
			String regExp) {
		String[] pieces = null;
		boolean restart = false;
		if (message.length() <= maximumSize) {
			pieces = new String[1];
			pieces[0] = message;
		} else {
			pieces = message.split(regExp);
			if (pieces.length > 1) {
				for (int i = 0; i < pieces.length; i++) {
					if (restart) {
						i = 0;
						restart = false;
					}
					String piece = pieces[i];
					if (piece.length() > maximumSize) {
						pieces = subdividePiece(pieces, i, regExp);
						i = 0;
					} else {
						if (i < pieces.length - 1) {
							if ((piece.length() + pieces[i + 1].length()) <= maximumSize) {
								String strippedRegExp = regExp.replaceAll(
										"\\\\", "");
								pieces = joinPieces(pieces, i, strippedRegExp);
								restart = true;
							}
						}
					}
				}
			} else {
				if (getNextRegExp(regExp) != null) {
					return splitMessage(message, maximumSize,
							getNextRegExp(regExp));
				} else {
					return splitArbitrarily(message, maximumSize);
				}
			}
		}
		return pieces;
	}

	private static String[] splitArbitrarily(String message, int maximumLength) {
		return message.split("(?<=\\G.{" + maximumLength + "})");
	}

	private static String[] subdividePiece(String[] pieces, int i, String regExp) {
		String[] newPiece = splitMessage(pieces[i], 4000, regExp);
		String[] newArray = new String[pieces.length + newPiece.length - 1];
		for (int j = 0; j < newArray.length; j++) {
			if (j != i) {
				newArray[i] = pieces[i];
			} else {
				for (int k = 0; k < newPiece.length; k++) {
					newArray[j] = newPiece[k];
					j++;
				}
			}
		}
		return newArray;

	}

	private static String[] joinPieces(String[] pieces, int i, String regExp) {
		String[] newArray = new String[pieces.length - 1];
		int positionPieces = 0;
		for (int j = 0; j < newArray.length; j++) {
			if (j != i) {
				newArray[j] = pieces[positionPieces];
				positionPieces++;
			} else {
				newArray[j] = pieces[positionPieces] + regExp + pieces[positionPieces + 1];
				positionPieces = positionPieces + 2;
			}
		}
		return newArray;
	}

	private static String getNextRegExp(String regExp) {
		for (int i = 0; i < REG_EXPS.length; i++) {
			String staticRegExp = REG_EXPS[i];
			if (staticRegExp.equals(regExp)) {
				if (i < REG_EXPS.length - 1) {
					return REG_EXPS[i + 1];
				}
			}
		}
		return null;
	}
}
