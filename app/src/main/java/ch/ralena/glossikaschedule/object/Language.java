package ch.ralena.glossikaschedule.object;

import ch.ralena.glossikaschedule.data.LanguageData;
import ch.ralena.glossikaschedule.data.LanguageType;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Language extends RealmObject {
	@PrimaryKey
	@Index
	private String languageId;

	RealmList<Pack> packs;

	public String getLanguageId() {
		return languageId;
	}

	public RealmList<Pack> getPacks() {
		return packs;
	}

	public Pack getPack(String book) {
		for (Pack pack : packs) {
			if (pack.getBook().equals(book)) {
				return pack;
			}
		}
		return null;
	}

	public String getLongName() {
		return getLanguageType().getName();
	}

	public LanguageType getLanguageType() {
		LanguageType languageType = null;
		for (LanguageType language : LanguageData.languages) {
			if (this.languageId.equals(language.getId())) {
				return language;
			}
		}
		return languageType;
	}

	public int getSentenceCount() {
		int numSentences = 0;
		for (Pack pack : packs) {
			numSentences += pack.getSentences().size();
		}
		return numSentences;
	}


	/**
	 * Looks for matching sentence packs in another language set.
	 *
	 * @param targetLanguage language to check for matching packs
	 * @return RealmList containing packs shared between both languages
	 */
	public RealmList<Pack> getMatchingPacks(Language targetLanguage) {
		RealmList<Pack> matchingPacks = new RealmList<>();
		for (Pack targetPack : packs) {
			for (Pack basePack : targetLanguage.getPacks()) {
				if (targetPack.getBook().equals(basePack.getBook())) {
					matchingPacks.add(targetPack);
					break;
				}
			}
		}
		return matchingPacks;
	}
}
