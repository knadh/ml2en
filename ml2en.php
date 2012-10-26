<?php

/*
	(Phonemic) Romanization of Malayalam script
	http://nadh.in/code/ml2en

	This algorithm transliterates Malayalam script to Roman characters ('Manglish')
	Some heuristics try to retain a certain level phonemic fairness


	This work is licensed under GPL v2
	___________________

	Kailash Nadh, 2012
	http://nadh.in
*/

class ml2en {

	private static $_vowels = array(
		"അ" => "a", "ആ" => "aa", "ഇ" => "i", "ഈ" => "ee", "ഉ" => "u", "ഊ" => "oo", "ഋ" => "ru",
		"എ" => "e", "ഏ" => "e", "ഐ" => "ai", "ഒ" => "o", "ഓ" => "o", "ഔ" => "au", "ഃ" => "ha"
	);

	private static $_compounds = array(
		"ക്ക" => "kk", "ഗ്ഗ" => "gg", "ങ്ങ" => "ng",
		"ക്ക" => "kk", "ച്ച" => "cch", "ജ്ജ" => "jj", "ഞ്ഞ" => "nj",
		"ട്ട" => "tt", "ണ്ണ" => "nn",
		"ത്ത" => "tth", "ദ്ദ" => "ddh", "ദ്ധ" => "ddh", "ന്ന" => "nn",
		"ന്ത" => "nth", "ങ്ക" => "nk", "ണ്ട" => "nd", "ബ്ബ" => "bb",
		"പ്പ" => "pp", "മ്മ" => "mm",
		"യ്യ" => "yy", "ല്ല" => "ll", "വ്വ" => "vv", "ശ്ശ" => "sh", "സ്സ" => "s",
		"ക്സ" => "ks", "ഞ്ച" => "nch", "ക്ഷ" => "ksh", "മ്പ" => "mp", "റ്റ" => "tt", "ന്റ" => "nt", "ന്ത" => "nth",
		"ന്ത്യ" => "nthy"
	);

	private static $_consonants = array(
		"ക" => "k", "ഖ" => "kh", "ഗ" => "g", "ഘ" => "gh", "ങ" => "ng",
		"ച" => "ch", "ഛ" => "chh", "ജ" => "j", "ഝ" => "jh", "ഞ" => "nj",
		"ട" => "t", "ഠ" => "dt", "ഡ" => "d", "ഢ" => "dd", "ണ" => "n",
		"ത" => "th", "ഥ" => "th", "ദ" => "d", "ധ" => "dh", "ന" => "n",
		"പ" => "p", "ഫ" => "ph", "ബ" => "b", "ഭ" => "bh", "മ" => "m",
		"യ" => "y", "ര" => "r", "ല" => "l", "വ" => "v",
		"ശ" => "sh", "ഷ" => "sh", "സ" => "s","ഹ" => "h",
		"ള" => "l", "ഴ" => "zh", "റ" => "r"
	);

	private static $_chil = array(
		"ൽ" => "l", "ൾ" => "l", "ൺ" => "n",
		"ൻ" => "n", "ർ" => "r", "ൿ" => "k"
	);

	private static $_modifiers = array(
		"ാ" => "aa", "ി" => "i", "ീ" => "ee",
		"ു" => "u", "ൂ" => "oo", "ൃ" => "ru",
		"െ" => "e", "േ" => "e", "ൈ" => "y",
		"ൊ" => "o", "ോ" => "o","ൌ" => "ou", "ൗ" => "au"
	);

	// ______ transliterate a malayalam string to english phonetically
	function transliterate($input) {
		// replace zero width non joiners
		$input = preg_replace("/\xE2\x80\x8C/u", '', $input);

		// replace modified compounds first
		$input = self::_replaceModifiedGlyphs(self::$_compounds, $input);

		// replace modified non-compounds
		$input = self::_replaceModifiedGlyphs(array_merge(self::$_vowels, self::$_consonants), $input);

		// replace unmodified compounds
		foreach(self::$_compounds as $k=>$v) {
			$input = preg_replace("/".$k."്([\w])/u", $v.'$1', $input);	// compounds ending in chandrakkala but not at the end of the word
			$input = preg_replace("/".$k."്/u", $v.'u', $input);	// compounds ending in chandrakkala have +'u' pronunciation
			$input = preg_replace("/".$k."/u", $v.'a', $input);	// compounds not ending in chandrakkala have +'a' pronunciation
		}

		// glyphs not ending in chandrakkala have +'a' pronunciation
		foreach(self::$_consonants as $k=>$v) {
			$input = preg_replace("/".$k."(?!്)/u", $v.'a$1', $input);
		}

		// glyphs ending in chandrakkala not at the end of a word
		foreach(self::$_consonants as $k=>$v) {
			$input = preg_replace("/".$k."്(?![\s\)\(\[\]\.;,\"'\/\\\%\!]|$)/iu", $v.'$1', $input);
		}

		// remaining glyphs ending in chandrakkala will be at end of words and have a +'u' pronunciation
		foreach(self::$_consonants as $k=>$v) {
			$input = preg_replace("/".$k."്/iu", $v.'u', $input);
		}

		// remaining self::$_consonants
		foreach(self::$_consonants as $k=>$v) {
			$input = preg_replace("/".$k."/u", $v, $input);
		}

		// vowels
		foreach(self::$_vowels as $k=>$v) {
			$input = preg_replace("/".$k."/u", $v, $input);
		}

		// chillu glyphs
		foreach(self::$_chil as $k=>$v) {
			$input = preg_replace("/".$k."/u", $v, $input);
		}

		// anusvaram 'am' at the end
		$input = preg_replace("/ം/u", 'm', $input);

		// replace any stray modifiers that may have been left out
		foreach(self::$_modifiers as $k=>$v) {
			$input = preg_replace("/".$k."/u", $v, $input);
		}

		// capitalize first letter of sentences for better aeshetics
		$input = preg_replace('/([.!?])\s*(\w)/e', "strtoupper('\\1 \\2')", ucfirst(strtolower($input)));

		return $input;
	}

	// ______ replace modified glyphs
	private static function _replaceModifiedGlyphs($glyphs, $input) {
		// see if a given set of glyphs have modifiers trailing them
		preg_match_all("/(".implode("|", array_keys($glyphs)).")(". implode('|', array_keys(self::$_modifiers) ).")/u", $input, $match);

		// if yes, replace the glpyh with it's roman equivalent, and the modifier with its
		if(isset($match[0])) {
			for($n=0; $n<count($match[0]); $n++) {
				$input = preg_replace("/".$match[0][$n]."/u", $glyphs[ $match[1][$n] ] . self::$_modifiers[ $match[2][$n] ], $input);
			}
		}

		return $input;
	}
}

?>