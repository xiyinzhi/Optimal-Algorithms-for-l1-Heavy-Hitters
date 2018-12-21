"""
Import
"""
import nltk
from nltk.stem.snowball import SnowballStemmer
# import enchant
from collections import Counter
from operator import itemgetter

"""
functions
"""


def cleanFile():
    read = open('wikidump.txt', 'r')
    write = open("small_wiki_streaming.txt", 'w')
    for s in read:
        # s = read.readline()
        words = nltk.word_tokenize(s)
        d = enchant.Dict("en_US")
        words = [word.lower() for word in words if (word.isalpha() and d.check(word))]

        for word in words:
            write.writelines(word)
            write.write("\n")

    read.close()
    write.close()


def generateSmallFile():
    read = open('../wiki_streaming.txt', 'r')
    write = open("../small_wiki_streaming.txt", 'w')
    for i in range(0, 10000000):
        s = read.readline()
        write.writelines(s)
        i += 1
    read.close()
    write.close()


def wordCount():
    read = open('../small_wiki_streaming.txt', 'r')
    write = open("small_wiki_sorted_word_count.txt", 'w')

    s = read.readlines()
    # s.replace("\n","")
    # words = nltk.word_tokenize(s)
    word_count_dict = Counter(w for w in s)
    sortedWords = sorted(word_count_dict.items(), key=itemgetter(1), reverse=True)

    for w in sortedWords:
        line = w[0].replace("\n", "")
        line += " " + str(w[1]) + "\n"
        # print(line)
        write.writelines(line)
        # print(w[0],w[1])

    read.close()
    write.close()


"""
Main
"""
if __name__ == "__main__":
    generateSmallFile()
    wordCount()
