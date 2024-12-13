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

# recommend jobs
recommendations = {}
for i, candidate_id in enumerate(candidate_skill['candidate_id']):
    # Lấy công việc có điểm tương đồng cao nhất cho từng ứng viên
    similar_jobs = similarity_matrix[i].argsort()[::-1]  # Sắp xếp giảm dần
    recommended_jobs = job_skill['job_id'].iloc[similar_jobs].tolist()
    recommendations[candidate_id] = recommended_jobs

# Kết quả gợi ý
# print("Gợi ý công việc:")
# for candidate, jobs in recommendations.items():
#     print(f"Ứng viên {candidate}: Công việc {jobs}")

# Save recommendation model
with open('recommendedJobs/recommendation_model.json', 'w') as f:
    json.dump(recommendations, f)