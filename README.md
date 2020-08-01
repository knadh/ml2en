# ml2en (Python, Javascript, PHP)
### An algorithm to transliterate Malayalam script to Roman / Latin characters (commonly 'Manglish') with reasonable phonetic fairness
Kailash Nadh, October 2012

Documentation: http://kailashnadh.name/code/ml2en

Licensed under GNU GPL v2 license.

# Example
### Input:
വ്യാഴത്തിന്റെ കാന്തികക്ഷേത്രം സൗരവാതത്തെ ചെറുക്കുന്ന മേഖലയാണ്‌ വ്യാഴത്തിന്റെ കാന്തമണ്ഡലം. സൂര്യനിലേക്കുള്ള ദിശയിൽ ഏതാണ്ട് എഴുപത് ലക്ഷം കിലോമീറ്ററും വിപരീത ദിശയിൽ ശനിയുടെ പരിക്രമണപഥം വരെയും ഇത് വ്യാപിച്ചുകിടക്കുന്നു. സൗരയൂഥത്തിലെ ഗ്രഹങ്ങളുടെ കാന്തമണ്ഡലങ്ങളിൽ വച്ച് ഏറ്റവും ശക്തിയേറിയതാണ്‌ വ്യാഴത്തിന്റേത്. സൗരമണ്ഡലം കഴിഞ്ഞാൽ സൗരയൂഥത്തിലെ ഏറ്റവും വലിയ ഘടനയും ഇതുതന്നെ. ഭൂമിയുടെ കാന്തമണ്ഡലത്തെക്കാൾ വീതിയേറിയതും പരന്നതുമായ വ്യാഴത്തിന്റെ കാന്തമണ്ഡലത്തിന്റെ ശക്തി ഭൂമിയൂടേതിന്റെ പത്തിരട്ടിയോളവും വ്യാപ്തം 18000 ഇരട്ടിയോളവുമാണ്‌.
### Output: 
Vyaazhatthinte kaanthikakshethram sauravaathatthe cherukkunna mekhalayaanu vyaazhatthinte kaanthamandalam. Sooryanilekkulla dishayil ethaandu ezhupathu laksham kilomeettarum vipareetha dishayil shaniyute parikramanapatham vareyum ithu vyaapicchukitakkunnu. Saurayoothatthile grahangalute kaanthamandalangalil vacchu ettavum shakthiyeriyathaanu vyaazhatthintethu. Sauramandalam kazhinjaal saurayoothatthile ettavum valiya ghatanayum ithuthanne. Bhoomiyute kaanthamandalatthekkaal veethiyeriyathum parannathumaaya vyaazhatthinte kaanthamandalatthinte shakthi bhoomiyootethinte patthirattiyolavum vyaaptham 18000 irattiyolavumaanu.

# Why?
(Phonetic) Romanisation of Malayalam script can work decently well with phonetic search algorithms (for example, along with the Metaphone or Soundex algorithms). Could also potentially help people learning the language.

# Usage
The algorithm's available in three different languages, Python, Javascript, and PHP.


### Python

Install with `pip3 install ml2en`

```python
from ml2en import ml2en
print(ml2en.transliterate(ml_str))
```

### Javascript
```javascript
<script src="ml2en.js"></script>
<script>
	var result = ml2en(ml_str);
</script>
```

### PHP
```php
<?php
	require 'ml2en.php';

	$result = ml2en::transliterate($ml_str);
?>
```
