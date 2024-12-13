import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import MultiLabelBinarizer
from sqlalchemy import create_engine
import pickle
import json

# Connect to DB
engine = create_engine("mariadb+pymysql://root:sapassword@localhost/works")

# Query
candidate_skill_query = """
    SELECT cs.can_id AS candidate_id, s.skill_name 
    FROM Candidate_Skill cs
    JOIN Skill s ON cs.skill_id = s.skill_id;
"""
job_skill_query = """
    SELECT js.job_id, s.skill_name 
    FROM Job_Skill js
    JOIN Skill s ON js.skill_id = s.skill_id;
"""

candidate_skill_df = pd.read_sql(candidate_skill_query, engine)
job_skill_df = pd.read_sql(job_skill_query, engine)

# Prepare Data
candidate_skill = candidate_skill_df.groupby('candidate_id')['skill_name'].apply(list).reset_index()
job_skill = job_skill_df.groupby('job_id')['skill_name'].apply(list).reset_index()

# Turn to vectors
mlb = MultiLabelBinarizer()
candidate_vectors = mlb.fit_transform(candidate_skill['skill_name'])
job_vectors = mlb.transform(job_skill['skill_name'])

# Cosine similarity
similarity_matrix = cosine_similarity(candidate_vectors, job_vectors)

def suggest_candidates_for_jobs(similarity_matrix, candidate_skill_df, job_skill):
    candidate_recommendations = {}

    for j, job_id in enumerate(job_skill['job_id']):
        # Lấy ứng viên có điểm tương đồng cao nhất cho công việc này
        similar_candidates = similarity_matrix[:, j].argsort()[::-1]  # Sắp xếp giảm dần
        recommended_candidates = candidate_skill_df['candidate_id'].iloc[similar_candidates].tolist()
        candidate_recommendations[job_id] = recommended_candidates

    return candidate_recommendations

candidate_recommendations = suggest_candidates_for_jobs(similarity_matrix, candidate_skill, job_skill)

# Save recommendation model
with open('recommendedJobs/recommendation_jobs_model.json', 'w') as f:
    json.dump(candidate_recommendations, f)