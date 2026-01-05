CREATE TABLE pix_payment (
  id UUID PRIMARY KEY,
  amount NUMERIC,
  status VARCHAR(30),
  created_at TIMESTAMP
);
