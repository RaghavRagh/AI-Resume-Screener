from fastapi import FastAPI
from app.routes import extract

app = FastAPI(title="AI Resume ML Service")

app.include_router(extract.router, prefix='/api')