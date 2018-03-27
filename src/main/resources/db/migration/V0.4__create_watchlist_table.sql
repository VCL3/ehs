CREATE TABLE IF NOT EXISTS watchlist (
  user_id UUID NOT NULL PRIMARY KEY,
  stock_trackings JSONB
);