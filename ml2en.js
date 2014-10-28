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

var ml2en = function(input) {
	var _vowels = {
		"അ": "a", "ആ": "aa", "ഇ": "i", "ഈ": "ee", "ഉ": "u", "ഊ": "oo", "ഋ": "ru",
		"എ": "e", "ഏ": "e", "ഐ": "ai", "ഒ": "o", "ഓ": "o", "ഔ": "au"
	};

	var _compounds = {
		"ക്ക": "kk", "ഗ്ഗ": "gg", "ങ്ങ": "ng",
		"ക്ക": "kk", "ച്ച": "cch", "ജ്ജ": "jj", "ഞ്ഞ": "nj",
		"ട്ട": "tt", "ണ്ണ": "nn",
		"ത്ത": "tth", "ദ്ദ": "ddh", "ദ്ധ": "ddh", "ന്ന": "nn",
		"ന്ത": "nth", "ങ്ക": "nk", "ണ്ട": "nd", "ബ്ബ": "bb",
		"പ്പ": "pp", "മ്മ": "mm",
		"യ്യ": "yy", "ല്ല": "ll", "വ്വ": "vv", "ശ്ശ": "sh", "സ്സ": "s",
		"ക്സ": "ks", "ഞ്ച": "nch", "ക്ഷ": "ksh", "മ്പ": "mp", "റ്റ": "tt", "ന്റ": "nt", "ന്ത": "nth",
		"ന്ത്യ": "nthy"
	};

	var _consonants = {
		"ക": "k", "ഖ": "kh", "ഗ": "g", "ഘ": "gh", "ങ": "ng",
		"ച": "ch", "ഛ": "chh", "ജ": "j", "ഝ": "jh", "ഞ": "nj",
		"ട": "t", "ഠ": "dt", "ഡ": "d", "ഢ": "dd", "ണ": "n",
		"ത": "th", "ഥ": "th", "ദ": "d", "ധ": "dh", "ന": "n",
		"പ": "p", "ഫ": "ph", "ബ": "b", "ഭ": "bh", "മ": "m",
		"യ": "y", "ര": "r", "ല": "l", "വ": "v",
		"ശ": "sh", "ഷ": "sh", "സ": "s","ഹ": "h",
		"ള": "l", "ഴ": "zh", "റ": "r"
	};

	var _chil = {
		"ൽ": "l", "ൾ": "l", "ൺ": "n",
		"ൻ": "n", "ർ": "r", "ൿ": "k"
	};

	var _modifiers = {
		"ു്": "u", "ാ": "aa", "ി": "i", "ീ": "ee",
		"ു": "u", "ൂ": "oo", "ൃ": "ru",
		"െ": "e", "േ": "e", "ൈ": "y",
		"ൊ": "o", "ോ": "o","ൌ": "ou", "ൗ": "au",
		"ഃ": "a" 
	};


	// ______ transliterate a malayalam string to english phonetically
	function transliterate(input) {
		// replace zero width non joiners
		input = input.replace(/[\u200B-\u200D\uFEFF]/g, '');

		// replace modified compounds first
		input = _replaceModifiedGlyphs(_compounds, input);

		// replace modified non-compounds
		input = _replaceModifiedGlyphs(_vowels, input);
		input = _replaceModifiedGlyphs(_consonants, input);

		var v = '';
		// replace unmodified compounds
		for(var k in _compounds) {
			if(!_compounds.hasOwnProperty(k)) continue;

			v = _compounds[k];

			input = input.replace( new RegExp(k + "്([\\w])", 'g'), v + '$1' );	// compounds ending in chandrakkala but not at the end of the word
			input = input.replace( new RegExp(k + "്", 'g'), v + 'u' );	// compounds ending in chandrakkala have +'u' pronunciation
			input = input.replace( new RegExp(k, 'g'), v + 'a' );	// compounds not ending in chandrakkala have +'a' pronunciation
		}

		// glyphs not ending in chandrakkala have +'a' pronunciation
		for(var k in _consonants) {
			if(!_consonants.hasOwnProperty(k)) continue;

			v = _consonants[k];
			input = input.replace( new RegExp(k + "(?!്)", 'g'), v + 'a' );
		}

		// glyphs ending in chandrakkala not at the end of a word
		for(var k in _consonants) {
			if(!_consonants.hasOwnProperty(k)) continue;

			v = _consonants[k];
			input = input.replace( new RegExp( k + "്(?![\\s\)\.;,\"'\/\\\%\!])", 'ig'), v );
		}

		// remaining glyphs ending in chandrakkala will be at end of words and have a +'u' pronunciation
		for(var k in _consonants) {
			if(!_consonants.hasOwnProperty(k)) continue;

			v = _consonants[k];
			input = input.replace( new RegExp(k + "്", 'g'), v + 'u' );
		}

		// remaining consonants
		for(var k in _consonants) {
			if(!_consonants.hasOwnProperty(k)) continue;

			v = _consonants[k];
			input = input.replace( new RegExp(k, 'g'), v );
		}

		// vowels
		for(var k in _vowels) {
			if(!_vowels.hasOwnProperty(k)) continue;

			v = _vowels[k];
			input = input.replace( new RegExp(k, 'g'), v );
		}

		// chillu glyphs
		for(var k in _chil) {
			if(!_chil.hasOwnProperty(k)) continue;

			v = _chil[k];
			input = input.replace( new RegExp(k, 'g'), v );
		}

		// anusvaram 'am' at the end
		input = input.replace( /ം/g, 'm');

		// replace any stray modifiers that may have been left out
		for(var k in _modifiers) {
			if(!_modifiers.hasOwnProperty(k)) continue;

			v = _modifiers[k];
			input = input.replace( new RegExp(k, 'g'), v );
		}

		// capitalize first letter of sentences for better aeshetics
		input = input.replace(/(^\s*\w|[\.\!\?]\s*\w)/g, function(c) { return c.toUpperCase(); });

		return input;
	}

	// ______ replace modified glyphs
	function _replaceModifiedGlyphs(glyphs, input) {
		// see if a given set of glyphs have modifiers trailing them
		var match = 0,
			re = new RegExp("(" + _getKeys(glyphs).join('|') + ")(" + _getKeys(_modifiers).join('|') + ")", 'g');

		// if yes, replace the glpyh with its roman equivalent, and the modifier with its
		while(match != null) {
			match = re.exec(input);
			if(match)
				input = input.replace( new RegExp(match[0], 'g'), glyphs[ match[1] ] + _modifiers[ match[2] ]);
		}

		return input;
	}

	// ______ get the keys of an object literal
	function _getKeys(o) {
		var keys = [];
		for(var k in o) {
			if(o.hasOwnProperty(k)) {
				keys.push(k);
			}
		}

		return keys;
	}

	// _____ construct
	return transliterate(input);
};