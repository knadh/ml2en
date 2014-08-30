#! /usr/bin/env ruby
# encoding: utf-8

<<INFO
	(Phonemic) Romanization of Malayalam script
	Original script by Kailash Nadh, 2012, http://nadh.in/code/ml2en

	This algorithm transliterates Malayalam script to Roman characters ('Manglish')
	Some heuristics try to retain a certain level phonemic fairness


	This work is licensed under GPL v2
INFO


class Ml2En
	class << self
    def __vowels 
    	{
    	"അ"=> "a", "ആ"=> "aa", "ഇ"=> "i", "ഈ"=> "ee", "ഉ"=> "u", "ഊ"=> "oo", "ഋ"=> "ru",
    	"എ"=> "e", "ഏ"=> "e", "ഐ"=> "ai", "ഒ"=> "o", "ഓ"=> "o", "ഔ"=> "au"	}
    end 
    
    def __compounds
    	{"ക്ക"=> "kk", "ഗ്ഗ"=> "gg", "ങ്ങ"=> "ng",
    	"ക്ക"=> "kk", "ച്ച"=> "cch", "ജ്ജ"=> "jj", "ഞ്ഞ"=> "nj",
    	"ട്ട"=> "tt", "ണ്ണ"=> "nn",
    	"ത്ത"=> "tth", "ദ്ദ"=> "ddh", "ദ്ധ"=> "ddh", "ന്ന"=> "nn",
    	"ന്ത"=> "nth", "ങ്ക"=> "nk", "ണ്ട"=> "nd", "ബ്ബ"=> "bb",
    	"പ്പ"=> "pp", "മ്മ"=> "mm",
    	"യ്യ"=> "yy", "ല്ല"=> "ll", "വ്വ"=> "vv", "ശ്ശ"=> "sh", "സ്സ"=> "s",
    	"ക്സ"=> "ks", "ഞ്ച"=> "nch", "ക്ഷ"=> "ksh", "മ്പ"=> "mp", "റ്റ"=> "tt", "ന്റ"=> "nt", "ന്ത"=> "nth",
    	"ന്ത്യ"=> "nthy"}
    end
    
    def __consonants
    	{"ക"=> "k", "ഖ"=> "kh", "ഗ"=> "g", "ഘ"=> "gh", "ങ"=> "ng",
    	"ച"=> "ch", "ഛ"=> "chh", "ജ"=> "j", "ഝ"=> "jh", "ഞ"=> "nj",
    	"ട"=> "t", "ഠ"=> "dt", "ഡ"=> "d", "ഢ"=> "dd", "ണ"=> "n",
    	"ത"=> "th", "ഥ"=> "th", "ദ"=> "d", "ധ"=> "dh", "ന"=> "n",
    	"പ"=> "p", "ഫ"=> "ph", "ബ"=> "b", "ഭ"=> "bh", "മ"=> "m",
    	"യ"=> "y", "ര"=> "r", "ല"=> "l", "വ"=> "v",
    	"ശ"=> "sh", "ഷ"=> "sh", "സ"=> "s","ഹ"=> "h",
    	"ള"=> "l", "ഴ"=> "zh", "റ"=> "r"}
    end

    def __chil  
    	{"ൽ"=> "l", "ൾ"=> "l", "ൺ"=> "n",
    	"ൻ"=> "n", "ർ"=> "r", "ൿ"=> "k"}
    end

  	def __modifiers 
  		 {
  		"ാ"=> "aa", "ി"=> "i", "ീ"=> "ee",
  		"ു"=> "u", "ൂ"=> "oo", "ൃ"=> "ru",
  		"െ"=> "e", "േ"=> "e", "ൈ"=> "y",
  		"ൊ"=> "o", "ോ"=> "o","ൌ"=> "ou", "ൗ"=> "au",
  		"ഃ"=> "a"}
  	end

	  # ______ transliterate a malayalam string to english phonetically	

  	def transliterate(input)
  		# replace zero width non joiners
  		input.gsub!("/\xE2\x80\x8C/u", '')
  		# replace modified compounds first
  		input = self._replaceModifiedGlyphs(self.__compounds, input)
  		# replace modified non-compounds
  		input = self._replaceModifiedGlyphs(self.__vowels.merge(self.__consonants), input)		
  		
  		v = ''
  		# replace unmodified compounds
  		
  		self.__compounds.each do |k,v|
  			input.gsub!( k + '്([\\w])', v + '\1')	# compounds ending in chandrakkala but not at the end of the word
  			input.gsub!( k + '്', v + 'u' )	# compounds ending in chandrakkala have +'u' pronunciation
  			input.gsub!( k, v + 'a' )	# compounds not ending in chandrakkala have +'a' pronunciation
  		end
  
  		# glyphs not ending in chandrakkala have +'a' pronunciation
  		self.__consonants.each do |k,v|
  			input.gsub!(Regexp.new( k+ '(?!്)'), v + 'a')
  		end
  
  		# glyphs ending in chandrakkala not at the end of a word
  		self.__consonants.each do |k,v|
  			input.gsub!( Regexp.new(k + "്(?![\\s\)\.;,\"'\/\\\%\!])"), v)
  		end
  
  		# remaining glyphs ending in chandrakkala will be at end of words and have a +'u' pronunciation
  		self.__consonants.each do |k,v|
  			input.gsub!(Regexp.new( k + "്"), v + 'u' )
  		end
  
  		# remaining consonants
  		self.__consonants.each do |k,v|
  			input.gsub!(Regexp.new( k), v )
  		end
  
  		# vowels
  		self.__vowels.each do |k,v|
  			input.gsub!( Regexp.new(k), v )
  		end
  
  		# chillu glyphs
  		self.__chil.each do |k,v|
  			input.gsub!( Regexp.new(k), v )
  		end
  
  		# anusvaram 'am' at the end
  		input.gsub!( 'ം', 'm')
  
  		# replace any stray modifiers that may have been left out
  		self.__modifiers.each do |k,v|
  			input.gsub!( k, v )
  		end
  
  		# capitalize first letter of sentences for better aeshetics
  
  		return input
  	end

	  # ______ replace modified glyphs
  	def _replaceModifiedGlyphs(glyphs, input)
  
  		# see if a given set of glyphs have modifiers trailing them
  		exp =  Regexp.new("((#{glyphs.keys.join("|")})(#{self.__modifiers.keys.join('|')}))")
  		matches = input.scan(exp)
  		# if yes, replace the glpyh with its roman equivalent, and the modifier with its
  		unless matches.empty? # != None=>
  			matches.each do |match|
  			input.gsub!( match[0], glyphs[match[1]] + self.__modifiers[ match[2] ])
  		end		
  
  		end		
  
  		return input
  	end
  end
end
