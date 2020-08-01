#!/usr/bin/env python

from setuptools import setup

setup(
	name="ml2en",
	version="1.0",
	description="A transliteration algorithm to transliterate Malayalam unicode to 'Manglish'",
	author="Kailash Nadh",
	author_email="kailash.nadh@gmail.com",
	url="https://github.com/knadh/ml2en",
	packages=['ml2en'],
	download_url="https://github.com/knadh/ml2en",
	license="GPLv2",
	classifiers=[
		"Intended Audience :: Developers",
		"Programming Language :: Python",
		"Natural Language :: English",
		"License :: OSI Approved :: GNU General Public License v2 (GPLv2)",
		"Programming Language :: Python :: 2.6",
		"Programming Language :: Python :: 2.7",
		"Topic :: Software Development :: Libraries :: Python Modules",
	]
)
