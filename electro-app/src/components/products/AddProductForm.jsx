import React from 'react'
import { useState,useEffect } from 'react'
import { createProduct } from '../../api/productApi';

// destructuring the prop 'onProductAdded'
function AddNewProductForm( {onProductAdded} ) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [category, setCategory] = useState('ACCESSORY');
    const [stock, setStock] = useState('');
    const [file, setFile] = useState(null);

    const [error,setError] =useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const categories = ['SMARTPHONE', 'LAPTOP', 'ACCESSORY'];

    const submitForm = async(e) =>{
        e.preventDefault();

        const formData = new FormData();
        formData.append('file', file || 'null.png'); // The actual file
        formData.append('name', name || 'unknown');
        formData.append('description', description || 'No description'); // Default if empty
        formData.append('price', price || 0);
        formData.append('category', category || 'ACCESSORY'); // Default category
        formData.append('stock', stock || 1);

        const createdProduct = await createProduct(formData);
        onProductAdded(createdProduct);

        //after adding new product, removing their value in form
        setName('');
        setPrice('');
        setStock('');
        setIsSubmitting(false);
    }

  return (
    <div className="add-new-product">
    <h2>Add New Product</h2>
    {error && <div className="error-message" style={{ color: 'red' }}>{error}</div>}

    <form onSubmit={submitForm}>
        <div className="form-group">
        <label htmlFor="name">File</label>
            <input
                type="file"
                onChange={(e) => setFile(e.target.files[0])}
                required
            />
        </div>
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

        <div className="form-group">
        <label htmlFor="category">Category:</label>
        <select
            id="category"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
        >
            <option value="">-- Select Category --</option>
            {categories.map((cat) => (
            <option key={cat} value={cat}>
                {cat}
            </option>
            ))}
        </select>
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

export default AddNewProductForm