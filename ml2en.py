#! /usr/bin/env python3
# -*- coding: utf-8 -*-

# Python 3 script

'''
	(Phonemic) Romanization of Malayalam script
	http://nadh.in/code/ml2en

	This algorithm transliterates Malayalam script to Roman characters ('Manglish')
	Some heuristics try to retain a certain level phonemic fairness


	This work is licensed under GPL v2
	___________________

	Kailash Nadh, 2012
	http://nadh.in
'''

import re

class ml2en:
	__vowels = {
		"അ": "a", "ആ": "aa", "ഇ": "i", "ഈ": "ee", "ഉ": "u", "ഊ": "oo", "ഋ": "ru",
		"എ": "e", "ഏ": "e", "ഐ": "ai", "ഒ": "o", "ഓ": "o", "ഔ": "au"
	}

	__compounds = {
		"ക്ക": "kk", "ഗ്ഗ": "gg", "ങ്ങ": "ng",
		"ക്ക": "kk", "ച്ച": "cch", "ജ്ജ": "jj", "ഞ്ഞ": "nj",
		"ട്ട": "tt", "ണ്ണ": "nn",
		"ത്ത": "tth", "ദ്ദ": "ddh", "ദ്ധ": "ddh", "ന്ന": "nn",
		"ന്ത": "nth", "ങ്ക": "nk", "ണ്ട": "nd", "ബ്ബ": "bb",
		"പ്പ": "pp", "മ്മ": "mm",
		"യ്യ": "yy", "ല്ല": "ll", "വ്വ": "vv", "ശ്ശ": "sh", "സ്സ": "s",
		"ക്സ": "ks", "ഞ്ച": "nch", "ക്ഷ": "ksh", "മ്പ": "mp", "റ്റ": "tt", "ന്റ": "nt", "ന്ത": "nth",
		"ന്ത്യ": "nthy"
	}

	__consonants = {
		"ക": "k", "ഖ": "kh", "ഗ": "g", "ഘ": "gh", "ങ": "ng",
		"ച": "ch", "ഛ": "chh", "ജ": "j", "ഝ": "jh", "ഞ": "nj",
		"ട": "t", "ഠ": "dt", "ഡ": "d", "ഢ": "dd", "ണ": "n",
		"ത": "th", "ഥ": "th", "ദ": "d", "ധ": "dh", "ന": "n",
		"പ": "p", "ഫ": "ph", "ബ": "b", "ഭ": "bh", "മ": "m",
		"യ": "y", "ര": "r", "ല": "l", "വ": "v",
		"ശ": "sh", "ഷ": "sh", "സ": "s","ഹ": "h",
		"ള": "l", "ഴ": "zh", "റ": "r"
	}

	__chil = {
		"ൽ": "l", "ൾ": "l", "ൺ": "n",
		"ൻ": "n", "ർ": "r", "ൿ": "k"
	}

	__modifiers = {
		"ു്": "u", "ാ": "aa", "ി": "i", "ീ": "ee",
		"ു": "u", "ൂ": "oo", "ൃ": "ru",
		"െ": "e", "േ": "e", "ൈ": "y",
		"ൊ": "o", "ോ": "o","ൌ": "ou", "ൗ": "au",
		"ഃ": "a"
	}


	# ______ transliterate a malayalam string to english phonetically
	def transliterate(self, input):
		# replace zero width non joiners
		input = re.sub(r'\xE2\x80\x8C', '', input)

		# replace modified compounds first
		input = self._replaceModifiedGlyphs(self.__compounds, input)

		# replace modified non-compounds
		input = self._replaceModifiedGlyphs(self.__vowels, input)
		input = self._replaceModifiedGlyphs(self.__consonants, input)
		
		v = ''
		# replace unmodified compounds
		for k, v in self.__compounds.items():
			input = re.sub( k + '്([\\w])', v + '\1', input )	# compounds ending in chandrakkala but not at the end of the word
			input = input.replace( k + '്', v + 'u' )	# compounds ending in chandrakkala have +'u' pronunciation
			input = input.replace( k, v + 'a' )	# compounds not ending in chandrakkala have +'a' pronunciation

		# glyphs not ending in chandrakkala have +'a' pronunciation
		for k, v in self.__consonants.items():
			input = re.sub( k + '(?!്)', v + 'a', input )

		# glyphs ending in chandrakkala not at the end of a word
		for k, v in self.__consonants.items():
			input = re.sub( k + "്(?![\\s\)\.;,\"'\/\\\%\!])", v, input )

		# remaining glyphs ending in chandrakkala will be at end of words and have a +'u' pronunciation
		for k, v in self.__consonants.items():
			input = input.replace( k + "്", v + 'u' )

		# remaining consonants
		for k, v in self.__consonants.items():
			input = input.replace( k, v )

		# vowels
		for k, v in self.__vowels.items():
			input = input.replace( k, v )

		# chillu glyphs
		for k, v in self.__chil.items():
			input = input.replace( k, v )

		# anusvaram 'am' at the end
		input = input.replace( 'ം', 'm')

		# replace any stray modifiers that may have been left out
		for k, v in self.__modifiers.items():
			input = input.replace( k, v )

		# capitalize first letter of sentences for better aeshetics
		chunks = re.split('([.!?] *)', input)
		input = ''.join([w.capitalize() for w in chunks])

		return input

	# ______ replace modified glyphs
	def _replaceModifiedGlyphs(self, glyphs, input):

		# see if a given set of glyphs have modifiers trailing them
		exp = re.compile( '((' + '|'.join(glyphs.keys()) + ')(' + '|'.join(self.__modifiers.keys()) + '))' )
		matches = exp.findall(input)

		# if yes, replace the glpyh with its roman equivalent, and the modifier with its
		if matches != None:
			for match in matches:
				input = input.replace( match[0], glyphs[match[1]] + self.__modifiers[ match[2] ]);

		return input