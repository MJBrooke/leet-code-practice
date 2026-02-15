/*
 Occasionally, a merchant's system might accidentally send the same transaction twice.
 Find all "potential duplicates."
 A potential duplicate is defined as a transaction with the same merchant_id, same amount, and same currency
  that occurred within 60 seconds of another transaction by that same merchant.
 */
