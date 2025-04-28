import React, { useState, useEffect } from 'react';
import { fetchProduct, updateProduct } from '../../api/productApi';

function UpdateProductForm( {productId, onProductUpdate} ) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [category, setCategory] = useState('');
    const [stock, setStock] = useState('');

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(false);
    // Convert to number if it comes as string
    const id = Number(productId);

    useEffect(() => {
        if (!id || isNaN(id)) {
            console.error('Invalid product ID:', productId);
            return;
          }

        const loadProduct = async () => {
            try {
                const data = await fetchProduct(id);
                if (!data) {
                    throw new Error("Product not found");
                }
                setName(data.name || '');
                setPrice(data.price || '');
                setStock(data.stock || '');
                setDescription(data.description || '');
                setCategory(data.category || '');

            } catch (error) {
                setError(error.message);
            }
        };
        loadProduct();
    }, [id]);

    const submitForm = async (e) => {
        e.preventDefault();

        const productData = {
            name: name || 'unknown',
            description: description || 'No description',
            price: parseFloat(price) || 0,
            category: category || 'ACCESSORY',
            stockQuantity: parseInt(stock) || 1,
        };

        setIsSubmitting(true);
        setError(null);
        try {
            const result = await updateProduct(productData, id);
            onProductUpdate(result);
        } catch (error) {
            console.error(error.message);
            setError(error.message);
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <div className="update-product">
            <h2>Update Product</h2>
            {error && <div className="error-message" style={{ color: 'red' }}>{error}</div>}

            <form onSubmit={submitForm}>
                <div className="form-group">
                    <label htmlFor="name">Product Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
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
                    {isSubmitting ? 'Updating...' : 'Update Product'}
                </button>
            </form>
        </div>
    )
}

export default UpdateProductForm;