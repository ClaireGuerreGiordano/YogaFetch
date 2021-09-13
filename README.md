# Fetch Yoga #

Fetch is a library used to access data from file systems, databases, web services ect... based on Cats Free monad.

It aims to simplify and increase efficiency of remote source reading.

Fetch is useful if you want to:
- avoid writing code for caching and batching
- request data from multiple source
- request data concurrently
- implicitly cache results

## How? ##

Fetch does so by:
- separating data fetch from execution
- building a tree with data dependencies (concurrency is achieved with applicative bind and sequentiality with monadic bind)

## How to use it? ##

Tell Fetch how to fetch the data by implementing the DataSource trait

DataSource[F, Identity, result] where F is an effect, Identity is the type of identity used for fetching and Result is the type of the data to be fetched.

Accepting a list of identities as batch method parameter gives fetch the ability to batch requests from same datasource.

Fetches can be sequenced using flatMap

## Batching ##

Independant fetches can be batched automatically using *cartesian*, *traverse* and *applicative* monads.

Whe querying different datasources, they are queried at the same time.

## Deduplication ##

If two independant requests ask for same id, fetch will deduplicate.