import React, { useState } from 'react'
import {  createProductNoFile } from '../../api/productApi';

function AddProductNoFile( {onProductAdded} ) {

  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [category, setCategory] = useState('');
  const [stock, setStock] = useState('');

  const [error,setError] =useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const submitForm = async(e) =>{
    e.preventDefault();
    setIsSubmitting(true);

  const productData =  {//array to be converten to JSON
    name : name || 'unknown',
    description : description || 'unknown',
    price : price || 0,
    category: category || 'ACCESSORY',
    stock: stock || 0,
  }

    const createdProduct = await createProductNoFile(productData);
    onProductAdded(createdProduct);

    //after adding new product, removing their value in form
    // setName('');
    // setPrice('');
    // setStock('');
    // setDescription('');
    setIsSubmitting(false);
  }

  return (
    <div className="add-new-product">
    <h2>Add New Product</h2>
    {error && <div className="error-message" style={{ color: 'red' }}>{error}</div>}

    <form onSubmit={submitForm}>
        <div className="form-group">
            <label htmlFor="name">Product Name:</label>
            <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}//setting value
                placeholder="Enter product name"
            />
        </div>

        <div className="form-group">
            <label htmlFor="price">Price ($):</label>
            <input
                type="number"
                id="price"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                placeholder="Enter price"
                step="0.01"
                min="0"
            />
        </div>

        <div className="form-group">
            <label htmlFor="stock">Stock Quantity:</label>
            <input
                type="number"
                id="stock"
                value={stock}
                onChange={(e) => setStock(e.target.value)}
                placeholder="Enter stock quantity"
                min="0"
            />
        </div>

        <button
            type="submit"
            disabled={isSubmitting}
            className="submit-button"
        >
            {isSubmitting ? 'Adding...' : 'Add Product'}
        </button>
    </form>
</div>
  )
}

export default AddProductNoFile