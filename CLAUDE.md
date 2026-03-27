# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

**Start Postgres:**
```
docker compose up -d
```

**Start the backend** (from `backend/`):
```
./gradlew bootRun
```

**Stop Postgres:**
```
docker compose down
```

**Check DB:**
```
psql -h localhost -U shop -d shop -c "SELECT id, name, stock FROM products;"
psql -h localhost -U shop -d shop -c "SELECT * FROM orders;"
```

**Open the store:**
```
open shop.html
```

## Architecture

```
claude/
├── shop.html          # Frontend — vanilla HTML/CSS/JS, fetches from backend API
├── index.html         # Tic-tac-toe game (unrelated)
├── docker-compose.yml # Postgres 16 on port 5432
└── backend/           # Spring Boot 3 + Gradle
    └── src/main/java/com/shop/
        ├── product/   # GET /api/products, GET /api/products/categories
        └── order/     # POST /api/orders
```

### Key design decisions
- **Cart is client-side only** — no sessions or auth needed. Only products and orders touch the backend.
- **Inventory is server-enforced** — `POST /api/orders` uses `SELECT ... FOR UPDATE` to lock product rows, validates stock, and decrements atomically in one transaction. Returns `409 Conflict` if any item exceeds available stock.
- **`ddl-auto: create-drop`** — tables are recreated on each backend restart (dev mode). `data.sql` re-seeds all 10 products. Switch to `validate` for production.
- **CORS** — `CorsConfig` allows all origins so `shop.html` can be opened directly as a `file://` URL.
- **Sold out UI** — the frontend shows a greyed "Sold Out" badge and a low-stock warning (≤5 remaining) based on the `stock` field returned by the API.
