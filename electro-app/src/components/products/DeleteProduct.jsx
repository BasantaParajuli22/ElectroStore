import React from 'react'
import { useState } from 'react';
import { deleteProduct } from '../../api/productApi';

function DeleteProduct( {productId, onDelete} ) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [product, setProduct] = useState( [] );
  const [error, setError] = useState( null );

  const handleDelete = async ( ) =>{
    setIsDeleting(true);
    try {
     const success =  await deleteProduct(Number(productId)); // Force numeric ID
     if (success) {
        onDelete(Number(productId));// Only update state after successful backend deletion
      }
    } catch (error) {
      setError(error.message);
      console.log(error.message);
    }finally{
      setIsDeleting(false);
    }
  }
  return (
    <div>
    <button
      onClick={handleDelete}
      disabled={isDeleting}
      style={{ backgroundColor: isDeleting ? '#ccc' : '#ff4444' }}
    >
      {isDeleting ? 'Deleting...' : 'Delete'}
    </button>
    {error && <p style={{ color: 'red' }}>{error}</p>}
  </div>
  )
}

export default DeleteProduct