# sentiment.py
# Demonstrates connecting to the twitter API and accessing the twitter stream
# Author: Vince Carpino
# Version 1.0
# Date: February 15, 2017

import twitter
import json
from urllib import unquote

term1 = raw_input("Enter the first search term: ")
term2 = raw_input("Enter the second search term: ")

CONSUMER_KEY = 'U4SMnPM8qEQ6iU2QtWvEmyu79'
CONSUMER_SECRET = 'AFSpsSAZqwpe5r8ZD5anHaTlsvpIr7cO4AageX7D1cd0pEfftL'
OAUTH_TOKEN = '2339881100-13WmHOa8ZOyy6LCYBsUVY015SYtYXyl9RVVtx9m'
OAUTH_TOKEN_SECRET = '7DZCjtBedlQBiSkgxPjjFosYbXmeU26VVK23WW0PaNE2h'

auth = twitter.oauth.OAuth(OAUTH_TOKEN, OAUTH_TOKEN_SECRET, CONSUMER_KEY, CONSUMER_SECRET)

twitter_api = twitter.Twitter(auth=auth)

count = 1000

search_results1 = twitter_api.search.tweets(q=term1, count=count)
statuses1 = search_results1['statuses']

search_results2 = twitter_api.search.tweets(q=term2, count=count)
statuses2 = search_results2['statuses']

# Iterate through 5 more batches of results by following the cursor

for _ in range(5):
    try:
        next_results1 = search_results1['search_metadata']['next_results']
        next_results2 = search_results2['search_metadata']['next_results']

    except KeyError, e: # No more results when next_results doesn't exist
        break

    # Create a dictionary from next_results, which has the following form:
    # ?max_id=313519052523986943&q=NCAA&include_entities=1
    kwargs1 = dict([ kv.split('=') for kv in next_results1[1:].split("&") ])
    kwargs2 = dict([ kv.split('=') for kv in next_results2[1:].split("&") ])

    search_results1 = twitter_api.search.tweets(**kwargs1)
    statuses1 += search_results1['statuses']

    search_results2 = twitter_api.search.tweets(**kwargs2)
    statuses2 += search_results2['statuses']

status_texts1 = [ status['text'] for status in statuses1 ]

status_texts2 = [ status['text'] for status in statuses2 ]

words1 = [ w for t in status_texts1 for w in t.split() ]

words2 = [ w for t in status_texts2 for w in t.split() ]

print "\nSentiment Analysis"
sent_file = open('AFINN-111.txt')

scores = {}
for line in sent_file:
    term, score  = line.split("\t")
    scores[term] = int(score)

score1 = 0
score2 = 0

for word in words1:
    uword = word.encode('utf-8')
    if uword in scores.keys():
        score1 = score1 + scores[word]

for word in words2:
    uword = word.encode('utf-8')
    if uword in scores.keys():
        score2 = score2 + scores[word]

print
print 'Sentiment score for ' + term1 + ': ' + str(float(score1))
print 'Sentiment score for ' + term2 + ': ' + str(float(score2))
print

if float(score1) > float(score2):
    print term1 + ' had a higher sentiment'

elif float(score1) < float(score2):
    print term2 + ' had a higher sentiment'

else:
    print 'equal sentiment'
