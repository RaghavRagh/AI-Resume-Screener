import spacy
import re

nlp = spacy.load("en_core_web_sm")

SKILL_MAP = {
    "backend": {
        "java", "spring", "spring boot", "hibernate",
        "node", "express", "sql", "postgresql", "mysql",
        "mongodb", "redis", "docker", "kubernetes"
    },
    "frontend": {
        "reactjs", "next.js", "javascript", "typescript",
        "html", "css", "tailwind", "redux", "reactquery"
    },
    "ml": {
        "python", "pandas", "numpy", "scikit-learn",
        "tensorflow", "pytorch", "nlp", "spacy"
    }
}

NORMALIZATION = {
    "springboot": "spring boot",
    "postgres": "postgresql",
    "js": "javascript",
    "ts": "typescript",
    "react": "ReactJS",
    "reactjs": "ReactJS",
    # "react.js": "ReactJS"
}

def normalize(text: str):
    text = text.lower()
    text = re.sub(r"[^a-z0-9.+ ]", " ", text)
    text = re.sub(r"\s+", " ", text)
    return text

def extract_skills(text: str):
    text = normalize(text)

    all_skills = set()
    categorized = {
        "backend": set(),
        "frontend": set(),
        "ml": set()
    }

    for role, skills in SKILL_MAP.items():
        for skill in skills:
            if skill in text:
                # print("before normalization skill:", skill)
                normalized = NORMALIZATION.get(skill.replace(" ", ""), skill)
                # print("after normalization skill:", normalized)
                all_skills.add(normalized)
                categorized[role].add(normalized)

    return {
        "skills": sorted(all_skills),
        "categorized_skills": {
            role: sorted(values)
            for role, values in categorized.items()
        }
    }
