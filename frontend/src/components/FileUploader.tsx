import React, { useState } from 'react';

const FileUploader: React.FC = () => {
  const [message, setMessage] = useState('');

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (!file || !file.name.endsWith('.csv')) {
      setMessage('Please upload a valid CSV file.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('http://localhost:8080/api/upload', {
        method: 'POST',
        body: formData
      });

      const text = await response.text();
      setMessage(text);
    } catch (error) {
      setMessage('Upload failed');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Upload CSV File</h2>
      <input type="file" accept=".csv" onChange={handleFileUpload} />
      <p>{message}</p>
    </div>
  );
};

export default FileUploader;
