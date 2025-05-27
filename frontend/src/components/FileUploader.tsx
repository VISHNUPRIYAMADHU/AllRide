import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';


const FileUploader: React.FC = () => {
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState<'success' | 'danger' | ''>('');
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState(false);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    setSelectedFile(file || null);
    setMessage('');
    setMessageType('');
  };

  const handleUpload = async () => {
    if (!selectedFile || !selectedFile.name.toLowerCase().endsWith('.csv')) {
      setMessage('Please upload a valid CSV file.');
      setMessageType('danger');
      return;
    }

    setIsUploading(true);
    setMessage('');
    setMessageType('');

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await fetch('http://localhost:8080/api/upload', {
        method: 'POST',
        body: formData,
      });

      const text = await response.text();

    if (response.ok) {
      setMessage(text || 'File uploaded successfully.');
      setMessageType('success');
    } else {
      setMessage(text || 'Upload failed with server error.');
      setMessageType('danger');
    }
    } catch (error) {
      setMessage(`Upload failed due to network or unexpected error`);
      setMessageType('danger');
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">Bulk User Import System</h1>

      <div className="mx-auto" style={{ maxWidth: '500px' }}>
        <div className="card shadow-sm p-4">
          <h4 className="mb-3">Upload CSV File</h4>

          <div className="mb-3">
            <input
              type="file"
              className="form-control"
              accept=".csv"
              onChange={handleFileChange}
              disabled={isUploading}
            />
          </div>

          <div className="d-flex justify-content-start">
            <button
              className="btn btn-primary"
              style={{ width: '120px' }}
              onClick={handleUpload}
              disabled={!selectedFile || isUploading}
            >
              {isUploading ? 'Uploading...' : 'Upload'}
            </button>
          </div>

          {message && (
            <div className={`alert alert-${messageType} mt-3`} role="alert">
              {message}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default FileUploader;
