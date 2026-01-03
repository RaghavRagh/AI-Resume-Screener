from fastapi import APIRouter, UploadFile, File
from app.utils.pdf_reader import extract_text_from_pdf
from app.services.skill_extractor import extract_skills

from app.services.embedding_service import semantic_similarity
from pydantic import BaseModel

router = APIRouter()

class SimilarityRequest(BaseModel):
    resume_text: str
    job_description: str

class TextExtractRequest(BaseModel):
    text: str

@router.post("/similarity")
async def compute_similarity(payload: SimilarityRequest):
    score = semantic_similarity(payload.resume_text, payload.job_description)
    return {"semantic_score": score}


@router.post("/extract")
async def extract_resume_data(file: UploadFile = File(...)):
    if file.content_type != "application/pdf":
        return {"error": "Invalid file type. Please upload a PDF file."}

    text = extract_text_from_pdf(file.file)
    skill_data = extract_skills(text)

    return {
        "text": text,
        "skills": skill_data["skills"],
        "categorized_skills": skill_data["categorized_skills"],
    }

@router.post("/extract_text")
async def extract_text(payload: TextExtractRequest):
    skill_data = extract_skills(payload.text)
    print("Extracted Skills text s:", skill_data)
    return skill_data